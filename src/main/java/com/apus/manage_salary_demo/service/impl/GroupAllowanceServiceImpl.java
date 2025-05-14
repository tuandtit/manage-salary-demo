package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.common.error.BusinessException;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupAllowanceServiceImpl implements GroupAllowanceService {
    GroupAllowanceRepository groupAllowanceRepository;
    GroupAllowanceMapper groupAllowanceMapper;

    @Override
    public GroupAllowanceDto create(GroupAllowanceDto dto) {
        validateDuplicateCode(dto.getCode());
        GroupAllowanceEntity entity = groupAllowanceMapper.toEntity(dto);

        if (dto.getParent() != null && dto.getParent().getId() != null) {
            GroupAllowanceEntity parent = existsGroupAllowance(dto.getParent().getId());
            entity.setParent(parent);
        }

        return saveAndReturn(entity);
    }

    @Override
    public GroupAllowanceDto update(GroupAllowanceDto dto) {

        if (dto.getId() == null) {
            throw new BusinessException("400", "id must be not null");
        }

        GroupAllowanceEntity entity = existsGroupAllowance(dto.getId());

        if (!dto.getCode().equals(entity.getCode()))
            validateDuplicateCode(dto.getCode());
        groupAllowanceMapper.update(dto, entity);

        if (dto.getParent() != null && dto.getParent().getId() != null) {
            GroupAllowanceEntity parent = existsGroupAllowance(dto.getParent().getId());
            entity.setParent(parent);
        }

        return saveAndReturn(entity);
    }

    @Override
    public void delete(Long id) {
        groupAllowanceRepository.deleteById(id);
    }

    @Override
    public GroupAllowanceDto getById(Long id) {
        return groupAllowanceMapper.toDto(existsGroupAllowance(id));
    }

    @Override
    public Page<GroupAllowanceDto> getAll(GroupAllowanceSearchRequest request) {
        return groupAllowanceRepository.findAll(request.specification(), request.pageable())
                .map(groupAllowanceMapper::toDto);

    }

    private void validateDuplicateCode(String code) {
        if (groupAllowanceRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "The allowance group already exists with the code: " + code);
        }
    }

    private GroupAllowanceDto saveAndReturn(GroupAllowanceEntity groupAllowanceEntity) {
        GroupAllowanceEntity save = groupAllowanceRepository.save(groupAllowanceEntity);
        return GroupAllowanceDto.builder()
                .id(save.getId())
                .build();
    }

    private GroupAllowanceEntity existsGroupAllowance(Long id) {
        return groupAllowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "GroupAllowance (or parent) not found with id " + id));
    }
}
