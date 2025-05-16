package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.GroupAllowanceDto;
import com.apus.manage_salary_demo.dto.request.search.GroupAllowanceSearchRequest;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import com.apus.manage_salary_demo.mapper.GroupAllowanceMapper;
import com.apus.manage_salary_demo.repository.GroupAllowanceRepository;
import com.apus.manage_salary_demo.service.GroupAllowanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupAllowanceServiceImpl implements GroupAllowanceService {
    GroupAllowanceRepository groupAllowanceRepository;
    GroupAllowanceMapper allowanceMapper;
    Translator translator;

    @Override
    @Transactional
    public BaseDto create(GroupAllowanceDto dto) {
        validateDuplicateCode(dto.getCode());
        GroupAllowanceEntity entity = allowanceMapper.toEntity(dto);

        if (dto.getParent() != null && dto.getParent().getId() != null) {
            GroupAllowanceEntity parent = existsGroupAllowance(dto.getParent().getId());
            entity.setParentId(parent.getId());
        }

        return saveAndReturn(entity);
    }

    @Override
    @Transactional
    public BaseDto update(GroupAllowanceDto dto) {

        if (dto.getId() == null) {
            throw new BusinessException("400", translator.toLocale("error.id.not.null"));
        }

        GroupAllowanceEntity entity = existsGroupAllowance(dto.getId());

        if (!dto.getCode().equals(entity.getCode()))
            validateDuplicateCode(dto.getCode());
        allowanceMapper.update(dto, entity);

        if (dto.getParent() != null && dto.getParent().getId() != null) {
            GroupAllowanceEntity parent = existsGroupAllowance(dto.getParent().getId());
            entity.setParentId(parent.getId());
        } else {
            entity.setParentId(null);
        }

        return saveAndReturn(entity);
    }

    @Override
    public void delete(Long id) {
        groupAllowanceRepository.deleteById(id);
    }

    @Override
    public GroupAllowanceDto getById(Long id) {
        GroupAllowanceEntity entity = existsGroupAllowance(id);
        GroupAllowanceDto dto = allowanceMapper.toDto(entity);

        if (entity.getParentId() != null) {
            GroupAllowanceEntity parent = existsGroupAllowance(entity.getParentId());
            dto.setParent(GroupAllowanceDto.ParentDto.builder()
                    .id(parent.getId())
                    .code(parent.getCode())
                    .name(parent.getName())
                    .build());
        }
        return dto;
    }

    @Override
    public Page<GroupAllowanceDto> getAll(GroupAllowanceSearchRequest request) {
        return groupAllowanceRepository.findAll(request.specification(), request.pageable())
                .map(allowanceMapper::toDto);

    }

    private void validateDuplicateCode(String code) {
        if (groupAllowanceRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    translator.toLocale("error.code.duplicate") + code);
        }
    }

    private BaseDto saveAndReturn(GroupAllowanceEntity groupAllowanceEntity) {
        GroupAllowanceEntity save = groupAllowanceRepository.save(groupAllowanceEntity);
        return BaseDto.builder()
                .id(save.getId())
                .build();
    }

    private GroupAllowanceEntity existsGroupAllowance(Long id) {
        return groupAllowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", translator.toLocale("error.resource.not.found") + id));
    }
}
