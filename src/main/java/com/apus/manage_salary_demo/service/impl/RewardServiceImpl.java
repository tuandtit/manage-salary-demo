package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.client.product.UomClient;
import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.client.resources.CurrencyClient;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.RewardDto;
import com.apus.manage_salary_demo.dto.request.search.RewardSearchRequest;
import com.apus.manage_salary_demo.entity.GroupRewardEntity;
import com.apus.manage_salary_demo.entity.RewardEntity;
import com.apus.manage_salary_demo.mapper.RewardMapper;
import com.apus.manage_salary_demo.repository.GroupRewardRepository;
import com.apus.manage_salary_demo.repository.RewardRepository;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RewardServiceImpl implements RewardService {
    //Helper
    ClientServiceHelper clientHelper;
    UomClient uomClient;
    CurrencyClient currencyClient;

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

        Map<Long, UomDto> uomMap = buildUomMapFromRewards(rewardEntities);
        Map<Long, CurrencyDto> currencyMap = buildCurrencyMapFromRewards(rewardEntities);

        List<RewardDto> dtoList = new ArrayList<>();
        for (RewardEntity reward : rewardEntities) {
            RewardDto dto = rewardMapper.toDto(reward);
            dto.setUom(uomMap.get(reward.getUomId()));
            dto.setCurrency(currencyMap.get(reward.getCurrencyId()));
            dtoList.add(dto);
        }
        return dtoList;
    }

    private Map<Long, CurrencyDto> buildCurrencyMapFromRewards(List<RewardEntity> allowances) {
        Set<Long> currencyIds = new HashSet<>();
        for (var reward : allowances) {
            if (reward.getCurrencyId() != null) {
                currencyIds.add(reward.getCurrencyId());
            }
        }
        return clientHelper.buildCurrencyMap(currencyIds);
    }

    private Map<Long, UomDto> buildUomMapFromRewards(List<RewardEntity> allowances) {
        Set<Long> uomIds = new HashSet<>();
        for (var reward : allowances) {
            if (reward.getUomId() != null) {
                uomIds.add(reward.getUomId());
            }
        }
        return clientHelper.buildUomMap(uomIds);
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
