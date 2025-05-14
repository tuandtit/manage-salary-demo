package com.apus.manage_salary_demo.service.impl;

import com.apus.manage_salary_demo.client.product.UomClient;
import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.client.resources.CurrencyClient;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.request.search.AllowanceSearchRequest;
import com.apus.manage_salary_demo.entity.AllowanceEntity;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import com.apus.manage_salary_demo.mapper.AllowanceMapper;
import com.apus.manage_salary_demo.repository.AllowanceRepository;
import com.apus.manage_salary_demo.repository.GroupAllowanceRepository;
import com.apus.manage_salary_demo.service.AllowanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AllowanceServiceImpl implements AllowanceService {
    //Client
    UomClient uomClient;
    CurrencyClient currencyClient;

    //Repository
    AllowanceRepository allowanceRepository;
    GroupAllowanceRepository groupAllowanceRepository;

    //Mapper
    AllowanceMapper allowanceMapper;

    @Override
    public AllowanceDto create(AllowanceDto dto) {
        validateDuplicateCode(dto.getCode());
        AllowanceEntity allowanceEntity = allowanceMapper.toEntity(dto);
        Long groupAllowanceId = dto.getGroupAllowance().getId();
        if (groupAllowanceId != null)
            allowanceEntity.setGroupAllowanceEntity(existsGroupAllowance(groupAllowanceId));
        return saveAndReturn(allowanceEntity);
    }

    @Override
    public AllowanceDto update(AllowanceDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("400", "id must be not null");
        }

        AllowanceEntity entity = existsAllowance(dto.getId());

        if (!dto.getCode().equals(entity.getCode()))
            validateDuplicateCode(dto.getCode());
        allowanceMapper.update(dto, entity);

        if (dto.getGroupAllowance() != null && dto.getGroupAllowance().getId() != null) {
            GroupAllowanceEntity parent = existsGroupAllowance(dto.getGroupAllowance().getId());
            entity.setGroupAllowanceEntity(parent);
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
        response.setUom(uomClient.getUomById(entity.getUomId()).getData());
        response.setCurrency(currencyClient.getCurrencyById(entity.getCurrencyId()).getData());
        return response;
    }

    @Override
    public Page<AllowanceDto> getAll(AllowanceSearchRequest request) {
        Page<AllowanceEntity> page = allowanceRepository.findAll(request.specification(), request.pageable());

        if (page.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), page.getPageable(), page.getTotalElements());
        }

        Set<Long> uomIds = new HashSet<>();
        for (AllowanceEntity allowanceEntity : page.getContent()) {
            Long uomId = allowanceEntity.getUomId();
            if (uomId != null) {
                uomIds.add(uomId);
            }
        }


        Map<Long, UomDto> uomMap = buildUomMap(uomIds);

        Set<Long> currencyIds = new HashSet<>();
        for (AllowanceEntity allowanceEntity : page.getContent()) {
            Long currencyId = allowanceEntity.getCurrencyId();
            if (currencyId != null) {
                currencyIds.add(currencyId);
            }
        }

        Map<Long, CurrencyDto> currencyMap = buildCurrencyMap(currencyIds);

        List<AllowanceDto> dtoList = page.stream().map(allowance -> {
            AllowanceDto allowanceDto = allowanceMapper.toDto(allowance);
            allowanceDto.setUom(uomMap.get(allowance.getUomId()));
            allowanceDto.setCurrency(currencyMap.get(allowance.getCurrencyId()));
            return allowanceDto;
        }).toList();

        return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
    }

    public Map<Long, UomDto> buildUomMap(Set<Long> uomIds) {
        if (uomIds == null || uomIds.isEmpty()) return Collections.emptyMap();

        List<UomDto> content = uomClient
                .getListUom(ConvertUtils.joinLongSet(uomIds))
                .getData().getContent();

        return content.stream()
                .collect(Collectors.toMap(UomDto::getId, Function.identity()));
    }

    private Map<Long, CurrencyDto> buildCurrencyMap(Set<Long> currencyIds) {
        if (currencyIds == null || currencyIds.isEmpty()) return Collections.emptyMap();

        List<CurrencyDto> content = currencyClient
                .getListCurrency(ConvertUtils.joinLongSet(currencyIds))
                .getData().getContent();

        return content.stream()
                .collect(Collectors.toMap(CurrencyDto::getId, Function.identity()));
    }

    private void validateDuplicateCode(String code) {
        if (allowanceRepository.existsByCode(code)) {
            throw new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "The allowance group already exists with the code: " + code);
        }
    }

    private AllowanceEntity existsAllowance(Long id) {
        return allowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Allowance not found with id: " + id));
    }

    private GroupAllowanceEntity existsGroupAllowance(Long id) {
        return groupAllowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "groupAllowance not found with id: " + id));
    }

    private AllowanceDto saveAndReturn(AllowanceEntity allowanceEntity) {
        AllowanceEntity save = allowanceRepository.save(allowanceEntity);
        return AllowanceDto.builder()
                .id(save.getId())
                .build();
    }

}
