package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.common.enums.ApplicableType;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.RewardDto;
import com.apus.manage_salary_demo.dto.RewardPolicyDto;
import com.apus.manage_salary_demo.dto.RewardPolicyLineDto;
import com.apus.manage_salary_demo.dto.request.search.RewardPolicySearchRequest;
import com.apus.manage_salary_demo.entity.RewardPolicyEntity;
import com.apus.manage_salary_demo.entity.RewardPolicyLineEntity;
import com.apus.manage_salary_demo.mapper.RewardPolicyLineMapper;
import com.apus.manage_salary_demo.mapper.RewardPolicyMapper;
import com.apus.manage_salary_demo.repository.RewardPolicyRepository;
import com.apus.manage_salary_demo.service.RewardPolicyService;
import com.apus.manage_salary_demo.service.RewardService;
import com.apus.manage_salary_demo.service.helper.ClientServiceHelper;
import com.apus.manage_salary_demo.service.helper.RewardPolicyLineService;
import com.apus.manage_salary_demo.service.helper.RewardPolicyTargetService;
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
public class RewardPolicyServiceImpl implements RewardPolicyService {
    //Repository
    RewardPolicyRepository policyRepository;

    //Mapper
    RewardPolicyMapper policyMapper;
    RewardPolicyLineMapper lineMapper;

    //Helper
    ClientServiceHelper clientHelper;
    RewardPolicyLineService lineService;
    RewardPolicyTargetService targetService;
    RewardService rewardService;

    //Translator
    Translator translator;

    @Override
    @Transactional
    public BaseDto create(RewardPolicyDto dto) {
        validateDuplicateCode(dto.getCode());

        RewardPolicyEntity entity = policyMapper.toEntity(dto);
        RewardPolicyEntity saved = policyRepository.save(entity);

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
    public BaseDto update(RewardPolicyDto dto) {
        RewardPolicyEntity entity = existsRewardPolicyEntity(dto.getId());

        if (!entity.getCode().equals(dto.getCode())) {
            validateDuplicateCode(dto.getCode());
        }

        policyMapper.update(dto, entity);

        RewardPolicyEntity saved = policyRepository.save(entity);

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

    private RewardPolicyEntity existsRewardPolicyEntity(Long id) {
        return policyRepository.findById(id).orElseThrow(
                () -> new BusinessException("404", "Reward policy not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        policyRepository.deleteById(id);
    }

    @Override
    public RewardPolicyDto getById(Long id) {
        RewardPolicyEntity entity = existsRewardPolicyEntity(id);
        RewardPolicyDto dto = policyMapper.toDto(entity);

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

        List<RewardPolicyLineEntity> lines = lineService.getLinesByPolicyId(entity.getId());
        Map<Long, RewardDto> rewardMap = buildAllowanceMapFromLines(lines);
        List<RewardPolicyLineDto> lineDtoList = new ArrayList<>();
        for (var line : lines) {
            RewardPolicyLineDto lineDto = lineMapper.toDto(line);
            lineDto.setReward(rewardMap.get(line.getRewardId()));
            lineDtoList.add(lineDto);
        }

        dto.setLines(lineDtoList);

        return dto;
    }

    private Map<Long, RewardDto> buildAllowanceMapFromLines(List<RewardPolicyLineEntity> lines) {
        Set<Long> rewardIds = new HashSet<>();
        for (var line : lines) {
            if (line.getRewardId() != null) {
                rewardIds.add(line.getRewardId());
            }
        }
        return buildAllowanceMap(rewardIds);
    }

    private Map<Long, RewardDto> buildAllowanceMap(Set<Long> rewardIds) {
        if (rewardIds == null || rewardIds.isEmpty()) return Collections.emptyMap();

        List<RewardDto> content = rewardService.getAllDetailByIds(rewardIds);

        return content.stream()
                .collect(Collectors.toMap(RewardDto::getId, Function.identity()));
    }

    @Override
    public Page<RewardPolicyDto> getAll(RewardPolicySearchRequest request) {
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
