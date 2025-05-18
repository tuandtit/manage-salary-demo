package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.GroupRewardDto;
import com.apus.manage_salary_demo.dto.RewardDto;
import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.dto.request.search.RewardSearchRequest;
import com.apus.manage_salary_demo.entity.GroupRewardEntity;
import com.apus.manage_salary_demo.entity.RewardEntity;
import com.apus.manage_salary_demo.mapper.RewardMapper;
import com.apus.manage_salary_demo.repository.GroupRewardRepository;
import com.apus.manage_salary_demo.repository.RewardRepository;
import com.apus.manage_salary_demo.service.GroupRewardService;
import com.apus.manage_salary_demo.service.RewardService;
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
public class RewardServiceImpl implements RewardService {
    //Helper
    ClientServiceHelper clientHelper;
    GroupRewardService groupRewardService;

    //Repository
    RewardRepository rewardRepository;
    GroupRewardRepository groupRewardRepository;

    //Mapper
    RewardMapper rewardMapper;

    //Other
    Translator translator;

    @Override
    @Transactional
    public BaseDto create(RewardDto dto) {
        validateDuplicateCode(dto.getCode());
        RewardEntity rewardEntity = rewardMapper.toEntity(dto);
        Long groupRewardId = dto.getGroupReward().getId();
        if (groupRewardId != null)
            rewardEntity.setGroupRewardId(existsGroupReward(groupRewardId).getId());
        return saveAndReturn(rewardEntity);
    }

    @Override
    @Transactional
    public BaseDto update(RewardDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("400", translator.toLocale("error.id.not.null"));
        }

        RewardEntity entity = existsReward(dto.getId());

        if (!dto.getCode().equals(entity.getCode()))
            validateDuplicateCode(dto.getCode());
        rewardMapper.update(dto, entity);

        if (dto.getGroupReward() != null && dto.getGroupReward().getId() != null) {
            GroupRewardEntity parent = existsGroupReward(dto.getGroupReward().getId());
            entity.setGroupRewardId(parent.getId());
        }

        return saveAndReturn(entity);
    }

    @Override
    public void delete(Long id) {
        rewardRepository.deleteById(id);
    }

    @Override
    public RewardDto getById(Long id) {
        RewardEntity entity = existsReward(id);
        RewardDto response = rewardMapper.toDto(entity);
        response.setUom(clientHelper.getUomById(entity.getUomId()));
        response.setCurrency(clientHelper.getCurrencyById(entity.getCurrencyId()));
        return response;
    }

    @Override
    public Page<RewardDto> getAll(RewardSearchRequest request) {
        return rewardRepository.findAll(request.specification(), request.pageable())
                .map(rewardMapper::toDto);
    }

    @Override
    public List<RewardDto> getAllDetailByIds(Set<Long> ids) {
        List<RewardEntity> rewardEntities = rewardRepository.findAllById(ids);
        Set<Long> currencyIds = new HashSet<>();
        Set<Long> uomIds = new HashSet<>();
        Set<Long> groupRewardIds = new HashSet<>();
        for (var reward : rewardEntities) {
            if (reward.getCurrencyId() != null) {
                currencyIds.add(reward.getCurrencyId());
            }
            if (reward.getUomId() != null) {
                uomIds.add(reward.getUomId());
            }
            if (reward.getGroupRewardId() != null) {
                groupRewardIds.add(reward.getGroupRewardId());
            }
        }

        Map<Long, UomDto> uomMap = clientHelper.buildUomMap(uomIds);
        Map<Long, CurrencyDto> currencyMap = clientHelper.buildCurrencyMap(currencyIds);
        Map<Long, GroupRewardDto> groupRewardMap = buildGroupRewardMap(groupRewardIds);

        List<RewardDto> dtoList = new ArrayList<>();
        for (RewardEntity reward : rewardEntities) {
            RewardDto dto = rewardMapper.toDto(reward);
            dto.setUom(uomMap.get(reward.getUomId()));
            dto.setCurrency(currencyMap.get(reward.getCurrencyId()));
            GroupRewardDto groupRewardDto = groupRewardMap.get(reward.getGroupRewardId());
            dto.setGroupReward(SimpleDto.builder()
                    .id(groupRewardDto.getId())
                    .code(groupRewardDto.getCode())
                    .name(groupRewardDto.getName())
                    .build());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private Map<Long, GroupRewardDto> buildGroupRewardMap(Set<Long> groupRewardIds) {
        if (groupRewardIds == null || groupRewardIds.isEmpty()) return Collections.emptyMap();

        List<GroupRewardDto> content = groupRewardService.getAllDetailByIds(groupRewardIds);

        return content.stream()
                .collect(Collectors.toMap(GroupRewardDto::getId, Function.identity()));
    }

    private void validateDuplicateCode(String code) {
        if (rewardRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    translator.toLocale("error.code.duplicate") + code);
        }
    }

    private RewardEntity existsReward(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Reward " + translator.toLocale("error.resource.not.found") + id));
    }

    private GroupRewardEntity existsGroupReward(Long id) {
        return groupRewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "groupReward " + translator.toLocale("error.resource.not.found") + id));
    }

    private BaseDto saveAndReturn(RewardEntity rewardEntity) {
        RewardEntity save = rewardRepository.save(rewardEntity);
        return BaseDto.builder()
                .id(save.getId())
                .build();
    }
}
