package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.dto.request.PayrollAllowanceLineRequest;
import com.apus.manage_salary_demo.entity.AllowanceEntity;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import com.apus.manage_salary_demo.mapper.AllowanceLineMapper;
import com.apus.manage_salary_demo.mapper.PayrollAllowanceLineMapper;
import com.apus.manage_salary_demo.repository.AllowanceRepository;
import com.apus.manage_salary_demo.repository.PayrollAllowanceLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PayrollAllowanceLineService {

    private final PayrollAllowanceLineRepository lineRepository;
    private final AllowanceRepository allowanceRepository;
    private final PayrollAllowanceLineMapper payrollAllowanceLineMapper;
    private final AllowanceLineMapper allowanceLineMapper;


    @Transactional
    public void saveLines(Long payrollId, List<PayrollAllowanceLineRequest> lines) {
        if (payrollId == null || lines == null || lines.isEmpty()) return;

        List<PayrollAllowanceLineEntity> entities = buildLines(payrollId, lines);

        lineRepository.saveAll(entities);
    }

    private List<PayrollAllowanceLineEntity> buildLines(Long payrollId, List<PayrollAllowanceLineRequest> lines) {
        return lines.stream()
                .map(dto -> {
                    PayrollAllowanceLineEntity entity = payrollAllowanceLineMapper.toEntity(dto);
                    AllowanceEntity allowanceEntity = existsAllowance(dto.getAllowance().getId());
                    if (!Objects.equals(allowanceEntity.getGroupAllowanceId(), dto.getGroupAllowance().getId()))
                        throw new BusinessException("400", "The allowance with ID: " + allowanceEntity.getId() + " does not belong to the allowance group with ID: " + dto.getGroupAllowance().getId());
                    entity.setPayrollId(payrollId);
                    entity.setGroupAllowanceId(allowanceEntity.getGroupAllowanceId());
                    entity.setAllowanceId(allowanceEntity.getId());
                    return entity;
                }).toList();
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

//    @Transactional
//    public void updateLines(Long policyId, List<RewardPolicyLineDto> incomingDtos) {
//        List<RewardPolicyLineEntity> currentEntities = getLinesByPolicyId(policyId);
//        Map<Long, RewardPolicyLineEntity> currentMap = mapEntitiesById(currentEntities);
//        Set<Long> incomingIds = extractIdsFromDtos(incomingDtos);
//
//        deleteRemovedEntities(currentEntities, incomingIds);
//        upsertEntities(policyId, incomingDtos, currentMap);
//    }
//
//    public List<RewardPolicyLineEntity> getLinesByPolicyId(Long policyId) {
//        return lineRepository.findByRewardPolicyId(policyId);
//    }
//
//    private Map<Long, RewardPolicyLineEntity> mapEntitiesById(List<RewardPolicyLineEntity> entities) {
//        return entities.stream()
//                .collect(Collectors.toMap(RewardPolicyLineEntity::getId, Function.identity()));
//    }
//
//    private Set<Long> extractIdsFromDtos(List<RewardPolicyLineDto> dtos) {
//        return dtos.stream()
//                .map(RewardPolicyLineDto::getId)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toSet());
//    }
//
//    private void deleteRemovedEntities(List<RewardPolicyLineEntity> currentEntities, Set<Long> incomingIds) {
//        for (var entity : currentEntities) {
//            if (!incomingIds.contains(entity.getId())) {
//                lineRepository.delete(entity);
//            }
//        }
//    }
//
//    private void upsertEntities(Long policyId, List<RewardPolicyLineDto> dtos, Map<Long, RewardPolicyLineEntity> currentMap) {
//        for (var dto : dtos) {
//            if (dto.getId() != null && currentMap.containsKey(dto.getId())) {
//                updateExistingEntity(dto, currentMap.get(dto.getId()));
//            } else {
//                createNewEntity(policyId, dto);
//            }
//        }
//    }
//
//    private void updateExistingEntity(RewardPolicyLineDto dto, RewardPolicyLineEntity entity) {
//        lineMapper.update(dto, entity);
//        if (dto.getReward().getId() != null) {
//            entity.setRewardId(existsAllowance(dto.getReward().getId()));
//        }
//        lineRepository.save(entity);
//    }
//
//    private void createNewEntity(Long policyId, RewardPolicyLineDto dto) {
//        RewardPolicyLineEntity newEntity = lineMapper.toEntity(dto);
//        newEntity.setRewardPolicyId(policyId);
//        newEntity.setRewardId(existsAllowance(dto.getReward().getId()));
//        lineRepository.save(newEntity);
//    }

    private AllowanceEntity existsAllowance(Long id) {
        return allowanceRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Allowance not found with id: " + id));
    }
}
