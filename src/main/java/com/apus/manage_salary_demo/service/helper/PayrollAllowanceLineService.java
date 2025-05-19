package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.AllowanceLineDto;
import com.apus.manage_salary_demo.dto.PayrollAllowanceLineDto;
import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.dto.request.PayrollAllowanceLineRequest;
import com.apus.manage_salary_demo.dto.response.PayrollAllowanceLineResponse;
import com.apus.manage_salary_demo.entity.AllowanceEntity;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import com.apus.manage_salary_demo.mapper.PayrollAllowanceLineMapper;
import com.apus.manage_salary_demo.repository.AllowanceRepository;
import com.apus.manage_salary_demo.repository.PayrollAllowanceLineRepository;
import com.apus.manage_salary_demo.service.AllowanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollAllowanceLineService {

    private final PayrollAllowanceLineRepository lineRepository;
    private final AllowanceRepository allowanceRepository;
    private final PayrollAllowanceLineMapper lineMapper;
    private final AllowanceService allowanceService;


    @Transactional
    public void saveLines(Long payrollId, List<PayrollAllowanceLineRequest> lines) {
        if (payrollId == null || lines == null || lines.isEmpty()) return;

        List<PayrollAllowanceLineEntity> entities = buildLines(payrollId, lines);

        lineRepository.saveAll(entities);
    }

    private List<PayrollAllowanceLineEntity> buildLines(Long payrollId, List<PayrollAllowanceLineRequest> lines) {
        return lines.stream()
                .map(dto -> {
                    PayrollAllowanceLineEntity entity = lineMapper.toEntity(dto);
                    var allowanceEntity = checkValidGroupAndAllowance(dto);
                    entity.setPayrollId(payrollId);
                    entity.setGroupAllowanceId(allowanceEntity.getGroupAllowanceId());
                    entity.setAllowanceId(allowanceEntity.getId());
                    return entity;
                }).toList();
    }

    private AllowanceEntity checkValidGroupAndAllowance(PayrollAllowanceLineRequest dto) {
        AllowanceEntity allowanceEntity = existsAllowance(dto.getAllowance().getId());
        if (!Objects.equals(allowanceEntity.getGroupAllowanceId(), dto.getGroupAllowance().getId()))
            throw new BusinessException("400", "The allowance with ID: " + allowanceEntity.getId() + " does not belong to the allowance group with ID: " + dto.getGroupAllowance().getId());
        return allowanceEntity;
    }

    public BigDecimal getTotalAllowanceAmount(List<PayrollAllowanceLineRequest> lines) {
        BigDecimal total = BigDecimal.ZERO;
        if (lines.isEmpty()) return total;

        for (var line : lines) {
            if (line != null)
                total = total.add(line.getAmount());

        }

        return total;
    }

    public void deleteByPayrollId(Long payrollId) {
        lineRepository.deleteByPayrollId(payrollId);
    }

    @Transactional
    public void updateLines(Long payrollId, List<PayrollAllowanceLineRequest> incomingDtos) {
        List<PayrollAllowanceLineEntity> currentEntities = lineRepository.findByPayrollId(payrollId);
        Map<Long, PayrollAllowanceLineEntity> currentMap = mapEntitiesById(currentEntities);
        Set<Long> incomingIds = extractIdsFromDtos(incomingDtos);

        deleteRemovedEntities(currentEntities, incomingIds);
        upsertEntities(payrollId, incomingDtos, currentMap);
    }

    private Map<Long, PayrollAllowanceLineEntity> mapEntitiesById(List<PayrollAllowanceLineEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(PayrollAllowanceLineEntity::getId, Function.identity()));
    }

    private Set<Long> extractIdsFromDtos(List<PayrollAllowanceLineRequest> dtos) {
        return dtos.stream()
                .map(PayrollAllowanceLineRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void deleteRemovedEntities(List<PayrollAllowanceLineEntity> currentEntities, Set<Long> incomingIds) {
        for (var entity : currentEntities) {
            if (!incomingIds.contains(entity.getId())) {
                lineRepository.delete(entity);
            }
        }
    }

    private void upsertEntities(Long payrollId, List<PayrollAllowanceLineRequest> dtos, Map<Long, PayrollAllowanceLineEntity> currentMap) {
        for (var dto : dtos) {
            if (dto.getId() != null && currentMap.containsKey(dto.getId())) {
                updateExistingEntity(dto, currentMap.get(dto.getId()));
            } else {
                createNewEntity(payrollId, dto);
            }
        }
    }

    private void updateExistingEntity(PayrollAllowanceLineRequest dto, PayrollAllowanceLineEntity entity) {
        lineMapper.update(dto, entity);
        if (dto.getAllowance().getId() != null) {
            AllowanceEntity allowanceEntity = checkValidGroupAndAllowance(dto);
            entity.setAllowanceId(allowanceEntity.getId());
            entity.setGroupAllowanceId(allowanceEntity.getGroupAllowanceId());
        }
        lineRepository.save(entity);
    }

    private void createNewEntity(Long payrollId, PayrollAllowanceLineRequest dto) {
        PayrollAllowanceLineEntity newEntity = lineMapper.toEntity(dto);
        var allowanceEntity = checkValidGroupAndAllowance(dto);
        newEntity.setPayrollId(payrollId);
        newEntity.setGroupAllowanceId(allowanceEntity.getGroupAllowanceId());
        newEntity.setAllowanceId(allowanceEntity.getId());
        lineRepository.save(newEntity);
    }

    public List<PayrollAllowanceLineDto> getPayrollAllowanceLineDtosByPayrollId(Long payrollId) {
        // Lấy danh sách entity từ service
        List<PayrollAllowanceLineEntity> lines = lineRepository.findByPayrollId(payrollId);

        // Xây allowanceMap (mỗi allowanceId -> AllowanceDto)
        Map<Long, AllowanceDto> allowanceMap = buildAllowanceMapFromLines(lines);

        // Chuyển thành List<PayrollAllowanceLineResponse>
        List<PayrollAllowanceLineResponse> responseList = new ArrayList<>();
        for (PayrollAllowanceLineEntity line : lines) {
            PayrollAllowanceLineResponse response = lineMapper.toDto(line);
            AllowanceDto allowance = allowanceMap.get(line.getAllowanceId());
            response.setAllowance(allowance);
            response.setGroupAllowance(allowance.getGroupAllowance());
            responseList.add(response);
        }

        // Nhóm theo groupAllowance và trả về kết quả
        return mapToGroupedDtos(responseList);
    }

    private List<PayrollAllowanceLineDto> mapToGroupedDtos(List<PayrollAllowanceLineResponse> responses) {
        if (responses == null || responses.isEmpty()) return Collections.emptyList();

        Map<SimpleDto, List<PayrollAllowanceLineResponse>> groupedMap = responses.stream()
                .collect(Collectors.groupingBy(PayrollAllowanceLineResponse::getGroupAllowance));

        return groupedMap.entrySet().stream()
                .map(entry -> {
                    SimpleDto group = entry.getKey();
                    List<AllowanceLineDto> allowanceLines = entry.getValue().stream()
                            .map(response -> AllowanceLineDto.builder()
                                    .id(response.getId())
                                    .allowance(response.getAllowance())
                                    .amountItem(response.getAmountItem())
                                    .amount(response.getAmount())
                                    .taxableAmount(response.getTaxableAmount())
                                    .insuranceAmount(response.getInsuranceAmount())
                                    .build())
                            .toList();

                    return PayrollAllowanceLineDto.builder()
                            .groupAllowance(group)
                            .allowanceLines(allowanceLines)
                            .build();
                })
                .toList();
    }

    private Map<Long, AllowanceDto> buildAllowanceMapFromLines(List<PayrollAllowanceLineEntity> lines) {
        Set<Long> allowanceIds = new HashSet<>();
        for (var line : lines) {
            if (line.getAllowanceId() != null) {
                allowanceIds.add(line.getAllowanceId());
            }
        }
        if (allowanceIds.isEmpty()) return Collections.emptyMap();

        List<AllowanceDto> content = allowanceService.getAllDetailByIds(allowanceIds);

        return content.stream()
                .collect(Collectors.toMap(AllowanceDto::getId, Function.identity()));
    }

    private AllowanceEntity existsAllowance(Long id) {
        return allowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Allowance not found with id: " + id));
    }
}
