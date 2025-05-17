package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.common.error.BusinessException;
import com.apus.manage_salary_demo.dto.request.PayrollAllowanceLineRequest;
import com.apus.manage_salary_demo.dto.request.PayrollRewardLineRequest;
import com.apus.manage_salary_demo.entity.PayrollRewardLineEntity;
import com.apus.manage_salary_demo.entity.RewardEntity;
import com.apus.manage_salary_demo.mapper.AllowanceLineMapper;
import com.apus.manage_salary_demo.mapper.PayrollRewardLineMapper;
import com.apus.manage_salary_demo.repository.PayrollRewardLineRepository;
import com.apus.manage_salary_demo.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PayrollRewardLineService {

    private final PayrollRewardLineRepository lineRepository;
    private final RewardRepository rewardRepository;
    private final PayrollRewardLineMapper payrollRewardLineMapper;
    private final AllowanceLineMapper allowanceLineMapper;


    @Transactional
    public void saveLines(Long payrollId, List<PayrollRewardLineRequest> lines) {
        if (payrollId == null || lines == null || lines.isEmpty()) return;

        List<PayrollRewardLineEntity> entities = buildLines(payrollId, lines);

        lineRepository.saveAll(entities);
    }

    private List<PayrollRewardLineEntity> buildLines(Long payrollId, List<PayrollRewardLineRequest> lines) {
        return lines.stream()
                .map(dto -> {
                    PayrollRewardLineEntity entity = payrollRewardLineMapper.toEntity(dto);
                    RewardEntity rewardEntity = existsAllowance(dto.getReward().getId());
                    if (!Objects.equals(rewardEntity.getGroupRewardId(), dto.getGroupReward().getId()))
                        throw new BusinessException("400", "The reward with ID: " + rewardEntity.getId() + " does not belong to the reward group with ID: " + dto.getGroupReward().getId());
                    entity.setPayrollId(payrollId);
                    entity.setGroupRewardId(rewardEntity.getGroupRewardId());
                    entity.setRewardId(rewardEntity.getId());
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

    private RewardEntity existsAllowance(Long id) {
        return rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException("404", "Reward not found with id: " + id));
    }
}
