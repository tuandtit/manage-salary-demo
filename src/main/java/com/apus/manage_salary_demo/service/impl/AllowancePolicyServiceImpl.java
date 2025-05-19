package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.common.enums.ApplicableType;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.AllowancePolicyDto;
import com.apus.manage_salary_demo.dto.AllowancePolicyLineDto;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.request.search.AllowancePolicySearchRequest;
import com.apus.manage_salary_demo.entity.AllowancePolicyEntity;
import com.apus.manage_salary_demo.entity.AllowancePolicyLineEntity;
import com.apus.manage_salary_demo.mapper.AllowancePolicyLineMapper;
import com.apus.manage_salary_demo.mapper.AllowancePolicyMapper;
import com.apus.manage_salary_demo.repository.AllowancePolicyRepository;
import com.apus.manage_salary_demo.service.AllowancePolicyService;
import com.apus.manage_salary_demo.service.AllowanceService;
import com.apus.manage_salary_demo.service.helper.AllowancePolicyLineService;
import com.apus.manage_salary_demo.service.helper.AllowancePolicyTargetService;
import com.apus.manage_salary_demo.service.helper.ClientServiceHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AllowancePolicyServiceImpl implements AllowancePolicyService {
    //Repository
    AllowancePolicyRepository policyRepository;

    //Mapper
    AllowancePolicyMapper policyMapper;
    AllowancePolicyLineMapper lineMapper;

    //Helper
    ClientServiceHelper clientHelper;
    AllowancePolicyLineService lineService;
    AllowancePolicyTargetService targetService;
    AllowanceService allowanceService;

    //Translator
    Translator translator;

    @Override
    @Transactional
    public BaseDto create(AllowancePolicyDto dto) {
        validateDuplicateCode(dto.getCode());

        AllowancePolicyEntity entity = policyMapper.toEntity(dto);
        AllowancePolicyEntity saved = policyRepository.save(entity);

        if (!entity.getApplicableType().equals(ApplicableType.ALL)) {
            targetService.saveTargets(saved.getId(), dto.getTargets());
        }

        lineService.saveLines(saved.getId(), dto.getLines());

        return BaseDto.builder()
                .id(saved.getId())
                .build();
    }

    @Override
    @Transactional
    public BaseDto update(AllowancePolicyDto dto) {
        AllowancePolicyEntity entity = existsAllowancePolicyEntity(dto.getId());

        if (!entity.getCode().equals(dto.getCode())) {
            validateDuplicateCode(dto.getCode());
        }

        policyMapper.update(dto, entity);

        AllowancePolicyEntity saved = policyRepository.save(entity);

        if (!saved.getApplicableType().equals(ApplicableType.ALL)) {
            targetService.updateTargets(dto.getId(), dto.getTargets());
        } else {
            targetService.updateTargets(dto.getId(), List.of());
        }

        lineService.updateLines(dto.getId(), dto.getLines());

        return BaseDto.builder()
                .id(saved.getId())
                .build();
    }

    private AllowancePolicyEntity existsAllowancePolicyEntity(Long id) {
        return policyRepository.findById(id).orElseThrow(
                () -> new BusinessException("404", "Allowance policy not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        lineService.deleteByPolicyId(id);
        targetService.deleteByPolicyId(id);
        policyRepository.deleteById(id);
    }

    @Override
    public AllowancePolicyDto getById(Long id) {
        AllowancePolicyEntity entity = existsAllowancePolicyEntity(id);
        AllowancePolicyDto dto = policyMapper.toDto(entity);

        if (entity.getApplicableType() != null && !entity.getApplicableType().equals(ApplicableType.ALL)) {
            Set<Long> targetIds = targetService.getTargetIdsByPolicyId(entity.getId());
            String ids = ConvertUtils.joinLongSet(targetIds);

            switch (entity.getApplicableType()) {
                case DEPARTMENT -> dto.setTargets(clientHelper.getAllDepartmentByIds(ids));
                case EMPLOYEE -> dto.setTargets(clientHelper.getAllEmployeeByIds(ids));
                case POSITION -> dto.setTargets(clientHelper.getAllPositionByIds(ids));
                default -> throw new BusinessException("400", "Applicable Type not valid");
            }
        }

        List<AllowancePolicyLineEntity> lines = lineService.getLinesByPolicyId(entity.getId());
        Map<Long, AllowanceDto> allowanceMap = buildAllowanceMapFromLines(lines);
        List<AllowancePolicyLineDto> lineDtoList = new ArrayList<>();
        for (var line : lines) {
            AllowancePolicyLineDto lineDto = lineMapper.toDto(line);
            lineDto.setAllowance(allowanceMap.get(line.getAllowanceId()));
            lineDtoList.add(lineDto);
        }

        dto.setLines(lineDtoList);

        return dto;
    }

    private Map<Long, AllowanceDto> buildAllowanceMapFromLines(List<AllowancePolicyLineEntity> lines) {
        Set<Long> allowanceIds = new HashSet<>();
        for (var line : lines) {
            if (line.getAllowanceId() != null) {
                allowanceIds.add(line.getAllowanceId());
            }
        }
        return buildAllowanceMap(allowanceIds);
    }

    private Map<Long, AllowanceDto> buildAllowanceMap(Set<Long> allowanceIds) {
        if (allowanceIds == null || allowanceIds.isEmpty()) return Collections.emptyMap();

        List<AllowanceDto> content = allowanceService.getAllDetailByIds(allowanceIds);

        return content.stream()
                .collect(Collectors.toMap(AllowanceDto::getId, Function.identity()));
    }

    @Override
    public Page<AllowancePolicyDto> getAll(AllowancePolicySearchRequest request) {
        return policyRepository.findAll(request.specification(), request.pageable())
                .map(policyMapper::toDto);
    }

    private void validateDuplicateCode(String code) {
        if (policyRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    translator.toLocale("error.code.duplicate") + code);
        }
    }
}
