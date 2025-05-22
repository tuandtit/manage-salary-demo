package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.common.utils.ConvertUtils;
import com.apus.manage_salary_demo.config.Translator;
import com.apus.manage_salary_demo.dto.AllowanceLineDto;
import com.apus.manage_salary_demo.dto.PayrollAllowanceLineDto;
import com.apus.manage_salary_demo.dto.SimpleAllowanceDto;
import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.entity.AllowanceEntity;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import com.apus.manage_salary_demo.mapper.PayrollAllowanceLineMapper;
import com.apus.manage_salary_demo.repository.AllowanceRepository;
import com.apus.manage_salary_demo.repository.GroupAllowanceRepository;
import com.apus.manage_salary_demo.repository.PayrollAllowanceLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollAllowanceLineService {

    private final PayrollAllowanceLineRepository lineRepository;
    private final AllowanceRepository allowanceRepository;
    private final GroupAllowanceRepository groupAllowanceRepository;
    private final PayrollAllowanceLineMapper lineMapper;
    private final ClientServiceHelper clientHelper;
    private final ConvertUtils convertUtils;
    private final Translator translator;

    @Transactional
    public void createOrUpdateLines(Long payrollId, Long groupAllowanceId, List<AllowanceLineDto> allowanceLineDtos) {
        if (ObjectUtils.isEmpty(allowanceLineDtos))
            deleteByPayrollId(payrollId);

        List<PayrollAllowanceLineEntity> currentEntities = lineRepository.findByPayrollIdAndGroupAllowanceId(payrollId, groupAllowanceId);
        Map<Long, PayrollAllowanceLineEntity> currentMap = mapEntitiesById(currentEntities);

        Set<Long> incomingIds = extractIdsFromDtos(allowanceLineDtos);
        if (!incomingIds.isEmpty())
            deleteRemovedEntities(currentEntities, incomingIds);

        List<PayrollAllowanceLineEntity> entities = new ArrayList<>();
        for (var allowanceLineDto : allowanceLineDtos) {
            if (Objects.isNull(allowanceLineDto.getId())) {
                entities.add(buildLine(payrollId, groupAllowanceId, allowanceLineDto));
            } else {
                entities.add(updateLine(groupAllowanceId, allowanceLineDto, currentMap));
            }
        }
        lineRepository.saveAll(entities);
    }

    private Map<Long, PayrollAllowanceLineEntity> mapEntitiesById(List<PayrollAllowanceLineEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(PayrollAllowanceLineEntity::getId, Function.identity()));
    }

    private Set<Long> extractIdsFromDtos(List<AllowanceLineDto> dtos) {
        return dtos.stream()
                .map(AllowanceLineDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private PayrollAllowanceLineEntity buildLine(Long payrollId, Long groupAllowanceId, AllowanceLineDto allowanceLine) {
        PayrollAllowanceLineEntity entity = lineMapper.toEntity(allowanceLine);
        var allowanceEntity = checkValidGroupAndAllowance(groupAllowanceId, allowanceLine.getAllowance().getId());
        entity.setPayrollId(payrollId);
        entity.setGroupAllowanceId(allowanceEntity.getGroupAllowanceId());
        entity.setAllowanceId(allowanceEntity.getId());
        return entity;
    }

    private PayrollAllowanceLineEntity updateLine(Long groupAllowanceId, AllowanceLineDto allowanceLineDto, Map<Long, PayrollAllowanceLineEntity> currentMap) {
        PayrollAllowanceLineEntity entity = currentMap.get(allowanceLineDto.getId());
        if (Objects.isNull(entity))
            throw new BusinessException("404", "PayrollAllowanceLineEntity " + translator.toLocale("error.resource.not.found") + allowanceLineDto.getId());
        checkValidGroupAndAllowance(groupAllowanceId, allowanceLineDto.getAllowance().getId());
        lineMapper.update(allowanceLineDto, entity);
        return entity;
    }

    private void deleteRemovedEntities(List<PayrollAllowanceLineEntity> currentEntities, Set<Long> incomingIds) {
        for (var entity : currentEntities) {
            if (!incomingIds.contains(entity.getId())) {
                lineRepository.delete(entity);
            }
        }
    }

    private AllowanceEntity checkValidGroupAndAllowance(Long groupAllowanceId, Long allowanceId) {
        AllowanceEntity allowanceEntity = existsAllowance(allowanceId);
        if (!Objects.equals(allowanceEntity.getGroupAllowanceId(), groupAllowanceId))
            throw new BusinessException("400", "The allowance with ID: " + allowanceEntity.getId() + " does not belong to the allowance group with ID: " + groupAllowanceId);
        return allowanceEntity;
    }

    public BigDecimal getTotalAllowanceAmount(List<PayrollAllowanceLineDto> lines) {
        BigDecimal total = BigDecimal.ZERO;
        if (lines.isEmpty()) return total;

        for (var line : lines) {
            for (var allowance : line.getAllowanceLines()) {
                if (allowance != null)
                    total = total.add(allowance.getAmount());
            }
        }

        return total;
    }

    public void deleteByPayrollId(Long payrollId) {
        lineRepository.deleteByPayrollId(payrollId);
    }

    public List<PayrollAllowanceLineDto> getPayrollAllowanceLineDtosByPayrollId(Long payrollId) {
        List<PayrollAllowanceLineEntity> lines = lineRepository.findByPayrollId(payrollId);
        Set<Long> allowanceIds = new HashSet<>();
        Set<Long> groupAllowanceIds = new HashSet<>();
        for (var line : lines) {
            if (line.getAllowanceId() != null) {
                allowanceIds.add(line.getAllowanceId());
            }
            if (line.getGroupAllowanceId() != null) {
                groupAllowanceIds.add(line.getGroupAllowanceId());
            }
        }
        Map<Long, SimpleAllowanceDto> allowanceMap = buildAllowanceMapFromLines(allowanceIds);
        Map<Long, SimpleDto> groupAllowanceMap = buildGroupAllowanceMapFromLines(groupAllowanceIds);

        List<AllowanceLineDto> responseList = new ArrayList<>();
        for (PayrollAllowanceLineEntity line : lines) {
            AllowanceLineDto response = lineMapper.toDto(line);
            response.setGroupAllowance(groupAllowanceMap.get(line.getGroupAllowanceId()));
            response.setAllowance(allowanceMap.get(line.getAllowanceId()));
            responseList.add(response);
        }

        // Nhóm theo groupAllowance và trả về kết quả
        return mapToGroupedDtos(responseList);
    }

    private Map<Long, SimpleDto> buildGroupAllowanceMapFromLines(Set<Long> groupAllowanceIds) {
        List<GroupAllowanceEntity> groupAllowanceEntities = groupAllowanceRepository.findAllById(groupAllowanceIds);
        List<SimpleDto> content = new ArrayList<>();
        for (var groupAllowance : groupAllowanceEntities) {
            content.add(SimpleDto.builder()
                    .id(groupAllowance.getId())
                    .code(groupAllowance.getCode())
                    .name(groupAllowance.getName())
                    .build());
        }
        return content.stream()
                .collect(Collectors.toMap(SimpleDto::getId, Function.identity()));
    }

    private Map<Long, SimpleAllowanceDto> buildAllowanceMapFromLines(Set<Long> allowanceIds) {
        if (allowanceIds.isEmpty()) return Collections.emptyMap();
        List<AllowanceEntity> allowanceEntities = allowanceRepository.findAllById(allowanceIds);

        Set<Long> currencyIds = new HashSet<>();
        for (var allowance : allowanceEntities) {
            if (allowance.getCurrencyId() != null) {
                currencyIds.add(allowance.getCurrencyId());
            }
        }
        Map<Long, CurrencyDto> currencyMap = clientHelper.buildCurrencyMap(currencyIds);
        List<SimpleAllowanceDto> content = new ArrayList<>();

        for (AllowanceEntity allowance : allowanceEntities) {
            List<String> includeTypes = allowance.getIncludeType() == null ? List.of() : convertUtils.convert(allowance.getIncludeType());
            content.add(SimpleAllowanceDto.builder()
                    .id(allowance.getId())
                    .code(allowance.getCode())
                    .includeTypes(includeTypes)
                    .currency(currencyMap.get(allowance.getCurrencyId()))
                    .build());
        }
        return content.stream()
                .collect(Collectors.toMap(SimpleAllowanceDto::getId, Function.identity()));
    }

    private List<PayrollAllowanceLineDto> mapToGroupedDtos(List<AllowanceLineDto> allowanceLineDtos) {
        if (allowanceLineDtos == null || allowanceLineDtos.isEmpty()) return Collections.emptyList();

        Map<SimpleDto, List<AllowanceLineDto>> groupedMap = allowanceLineDtos.stream()
                .collect(Collectors.groupingBy(AllowanceLineDto::getGroupAllowance));

        return groupedMap.entrySet().stream()
                .map(entry -> {
                    SimpleDto group = entry.getKey();
                    List<AllowanceLineDto> allowanceLines = entry.getValue();

                    return PayrollAllowanceLineDto.builder()
                            .groupAllowance(group)
                            .allowanceLines(allowanceLines)
                            .build();
                })
                .toList();
    }

    private AllowanceEntity existsAllowance(Long id) {
        if (Objects.isNull(id))
            throw new BusinessException("400", translator.toLocale("error.id.not.null"));
        return allowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Allowance " + translator.toLocale("error.resource.not.found") + id));
    }
}
