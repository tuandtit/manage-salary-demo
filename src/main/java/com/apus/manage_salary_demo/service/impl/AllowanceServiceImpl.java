package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.request.search.AllowanceSearchRequest;
import com.apus.manage_salary_demo.entity.AllowanceEntity;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import com.apus.manage_salary_demo.mapper.AllowanceMapper;
import com.apus.manage_salary_demo.repository.AllowanceRepository;
import com.apus.manage_salary_demo.repository.GroupAllowanceRepository;
import com.apus.manage_salary_demo.service.AllowanceService;
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
public class AllowanceServiceImpl implements AllowanceService {
    //Helper
    ClientServiceHelper clientHelper;

    //Repository
    AllowanceRepository allowanceRepository;
    GroupAllowanceRepository groupAllowanceRepository;

    //Mapper
    AllowanceMapper allowanceMapper;

    //Translator
    Translator translator;

    @Override
    @Transactional
    public BaseDto create(AllowanceDto dto) {
        validateDuplicateCode(dto.getCode());
        AllowanceEntity allowanceEntity = allowanceMapper.toEntity(dto);
        Long groupAllowanceId = dto.getGroupAllowance().getId();
        if (groupAllowanceId != null)
            allowanceEntity.setGroupAllowanceId(existsGroupAllowance(groupAllowanceId).getId());
        return saveAndReturn(allowanceEntity);
    }

    @Override
    @Transactional
    public BaseDto update(AllowanceDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("400", translator.toLocale("error.id.not.null"));
        }

        AllowanceEntity entity = existsAllowance(dto.getId());

        if (!dto.getCode().equals(entity.getCode()))
            validateDuplicateCode(dto.getCode());
        allowanceMapper.update(dto, entity);

        if (dto.getGroupAllowance() != null && dto.getGroupAllowance().getId() != null) {
            GroupAllowanceEntity parent = existsGroupAllowance(dto.getGroupAllowance().getId());
            entity.setGroupAllowanceId(parent.getId());
        }

        return saveAndReturn(entity);
    }

    @Override
    public void delete(Long id) {
        allowanceRepository.deleteById(id);
    }

    @Override
    public AllowanceDto getById(Long id) {
        AllowanceEntity entity = existsAllowance(id);
        AllowanceDto response = allowanceMapper.toDto(entity);
        response.setUom(clientHelper.getUomById(entity.getUomId()));
        response.setCurrency(clientHelper.getCurrencyById(entity.getCurrencyId()));
        return response;
    }

    @Override
    public Page<AllowanceDto> getAll(AllowanceSearchRequest request) {
        return allowanceRepository.findAll(request.specification(), request.pageable())
                .map(allowanceMapper::toDto);
    }

    @Override
    public List<AllowanceDto> getAllDetailByIds(Set<Long> ids) {
        List<AllowanceEntity> allowanceEntities = allowanceRepository.findAllById(ids);

        Set<Long> currencyIds = new HashSet<>();
        Set<Long> uomIds = new HashSet<>();

        for (var allowance : allowanceEntities) {
            if (allowance.getCurrencyId() != null) {
                currencyIds.add(allowance.getCurrencyId());
            }
            if (allowance.getUomId() != null) {
                uomIds.add(allowance.getUomId());
            }
        }

        Map<Long, UomDto> uomMap = clientHelper.buildUomMap(uomIds);
        Map<Long, CurrencyDto> currencyMap = clientHelper.buildCurrencyMap(currencyIds);

        List<AllowanceDto> dtoList = new ArrayList<>();
        for (AllowanceEntity allowance : allowanceEntities) {
            AllowanceDto dto = allowanceMapper.toDto(allowance);
            dto.setUom(uomMap.get(allowance.getUomId()));
            dto.setCurrency(currencyMap.get(allowance.getCurrencyId()));
            dtoList.add(dto);
        }
        return dtoList;
    }

    private void validateDuplicateCode(String code) {
        if (allowanceRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    translator.toLocale("error.code.duplicate") + code);
        }
    }

    private AllowanceEntity existsAllowance(Long id) {
        return allowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Allowance " + translator.toLocale("error.resource.not.found") + id));
    }

    private GroupAllowanceEntity existsGroupAllowance(Long id) {
        return groupAllowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "groupAllowance " + translator.toLocale("error.resource.not.found") + id));
    }

    private BaseDto saveAndReturn(AllowanceEntity allowanceEntity) {
        AllowanceEntity save = allowanceRepository.save(allowanceEntity);
        return BaseDto.builder()
                .id(save.getId())
                .build();
    }

}
