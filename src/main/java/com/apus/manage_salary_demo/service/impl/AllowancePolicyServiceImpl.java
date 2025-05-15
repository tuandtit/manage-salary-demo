package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.client.dto.BaseResponse;
import com.apus.manage_salary_demo.client.resources.DepartmentClient;
import com.apus.manage_salary_demo.client.resources.EmployeeClient;
import com.apus.manage_salary_demo.client.resources.PositionClient;
import com.apus.manage_salary_demo.client.resources.dto.TargetDto;
import com.apus.manage_salary_demo.common.enums.ApplicableType;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.dto.*;
import com.apus.manage_salary_demo.dto.request.search.AllowancePolicySearchRequest;
import com.apus.manage_salary_demo.dto.response.PagingResponse;
import com.apus.manage_salary_demo.entity.AllowancePolicyEntity;
import com.apus.manage_salary_demo.entity.AllowancePolicyLineEntity;
import com.apus.manage_salary_demo.mapper.AllowancePolicyLineMapper;
import com.apus.manage_salary_demo.mapper.AllowancePolicyMapper;
import com.apus.manage_salary_demo.repository.AllowancePolicyRepository;
import com.apus.manage_salary_demo.service.AllowancePolicyService;
import com.apus.manage_salary_demo.service.AllowanceService;
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

    //Service
    AllowancePolicyLineService lineService;
    AllowancePolicyTargetService targetService;
    AllowanceService allowanceService;

    //Client
    DepartmentClient departmentClient;
    PositionClient positionClient;
    EmployeeClient employeeClient;

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
    public void delete(Long id) {
        policyRepository.deleteById(id);
    }

    @Override
    public AllowancePolicyDto getById(Long id) {
        AllowancePolicyEntity entity = existsAllowancePolicyEntity(id);
        AllowancePolicyDto dto = policyMapper.toDto(entity);

        if (entity.getApplicableType() != null && !entity.getApplicableType().equals(ApplicableType.ALL)) {
            Set<Long> targetIdsByPolicyId = targetService.getTargetIdsByPolicyId(entity.getId());
            String ids = ConvertUtils.joinLongSet(targetIdsByPolicyId);

            switch (entity.getApplicableType()) {
                case DEPARTMENT -> dto.setTargets(getTargetDto(departmentClient.getAllDepartmentByIds(ids)));
                case EMPLOYEE -> dto.setTargets(getTargetDto(employeeClient.getAllEmployeeByIds(ids)));
                case POSITION -> dto.setTargets(getTargetDto(positionClient.getAllPositionByIds(ids)));
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

    private List<AllowanceTargetDto> getTargetDto(BaseResponse<PagingResponse<TargetDto>> allTargetByIds) {
        List<TargetDto> content = allTargetByIds.getData().getContent();
        List<AllowanceTargetDto> targetDtoList = new ArrayList<>();
        for (var dto : content) {
            targetDtoList.add(AllowanceTargetDto.builder()
                    .target(dto)
                    .build());
        }
        return targetDtoList;
    }

    @Override
    public Page<AllowancePolicyDto> getAll(AllowancePolicySearchRequest request) {
        return policyRepository.findAll(request.specification(), request.pageable())
                .map(policyMapper::toDto);
    }

    private void validateDuplicateCode(String code) {
        if (policyRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "The allowance policy already exists with the code: " + code);
        }
    }
}
