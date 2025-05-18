package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.GroupRewardDto;
import com.apus.manage_salary_demo.dto.request.search.GroupRewardSearchRequest;
import com.apus.manage_salary_demo.entity.GroupRewardEntity;
import com.apus.manage_salary_demo.mapper.GroupRewardMapper;
import com.apus.manage_salary_demo.repository.GroupRewardRepository;
import com.apus.manage_salary_demo.service.GroupRewardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupRewardServiceImpl implements GroupRewardService {
    GroupRewardRepository rewardRepository;
    GroupRewardMapper rewardMapper;
    Translator translator;

    @Override
    @Transactional
    public BaseDto create(GroupRewardDto dto) {
        validateDuplicateCode(dto.getCode());
        GroupRewardEntity entity = rewardMapper.toEntity(dto);

        if (dto.getParent() != null && dto.getParent().getId() != null) {
            GroupRewardEntity parent = existsGroupReward(dto.getParent().getId());
            entity.setParentId(parent.getId());
        }

        return saveAndReturn(entity);
    }

    @Override
    @Transactional
    public BaseDto update(GroupRewardDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("400", translator.toLocale("error.id.not.null"));
        }

        GroupRewardEntity entity = existsGroupReward(dto.getId());

        if (!dto.getCode().equals(entity.getCode()))
            validateDuplicateCode(dto.getCode());
        rewardMapper.update(dto, entity);

        if (dto.getParent() != null && dto.getParent().getId() != null) {
            GroupRewardEntity parent = existsGroupReward(dto.getParent().getId());
            entity.setParentId(parent.getId());
        } else {
            entity.setParentId(null);
        }

        return saveAndReturn(entity);
    }

    @Override
    public void delete(Long id) {
        rewardRepository.deleteById(id);
    }

    @Override
    public GroupRewardDto getById(Long id) {
        GroupRewardEntity entity = existsGroupReward(id);
        GroupRewardDto dto = rewardMapper.toDto(entity);

        if (entity.getParentId() != null) {
            GroupRewardEntity parent = existsGroupReward(entity.getParentId());
            dto.setParent(GroupRewardDto.ParentDto.builder()
                    .id(parent.getId())
                    .code(parent.getCode())
                    .name(parent.getName())
                    .build());
        }
        return dto;
    }

    @Override
    public Page<GroupRewardDto> getAll(GroupRewardSearchRequest request) {
        return rewardRepository.findAll(request.specification(), request.pageable())
                .map(rewardMapper::toDto);
    }

    @Override
    public List<GroupRewardDto> getAllDetailByIds(Set<Long> ids) {
        List<GroupRewardEntity> groupRewardEntities = rewardRepository.findAllById(ids);
        return rewardMapper.toDto(groupRewardEntities);
    }

    private void validateDuplicateCode(String code) {
        if (rewardRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    translator.toLocale("error.code.duplicate") + code);
        }
    }

    private BaseDto saveAndReturn(GroupRewardEntity rewardEntity) {
        GroupRewardEntity save = rewardRepository.save(rewardEntity);
        return BaseDto.builder()
                .id(save.getId())
                .build();
    }

    private GroupRewardEntity existsGroupReward(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "GroupReward " + translator.toLocale("error.resource.not.found") + id));
    }
}
