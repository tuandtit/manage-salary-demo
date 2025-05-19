package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.dto.ApplicableTargetDto;
import com.apus.manage_salary_demo.entity.RewardPolicyApplicableTargetEntity;
import com.apus.manage_salary_demo.repository.RewardPolicyTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RewardPolicyTargetService {

    private final RewardPolicyTargetRepository targetRepository;

    @Transactional
    public void saveTargets(Long policyId, List<ApplicableTargetDto> targets) {
        if (policyId == null || targets == null || targets.isEmpty()) return;

        List<RewardPolicyApplicableTargetEntity> entities = buildTargets(policyId, targets);

        targetRepository.saveAll(entities);
    }

    public void deleteByPolicyId(Long policyId) {
        targetRepository.deleteByRewardPolicyId(policyId);
    }

    @Transactional
    public void updateTargets(Long policyId, List<ApplicableTargetDto> incomingDtos) {
        if (policyId == null) return;

        // Xóa toàn bộ target hiện tại của policy
        targetRepository.deleteByRewardPolicyId(policyId);

        // Nếu danh sách mới không rỗng thì tạo lại
        if (incomingDtos != null && !incomingDtos.isEmpty()) {
            List<RewardPolicyApplicableTargetEntity> newEntities = buildTargets(policyId, incomingDtos);
            targetRepository.saveAll(newEntities);
        }
    }

    private List<RewardPolicyApplicableTargetEntity> buildTargets(Long policyId, List<ApplicableTargetDto> targets) {
        List<RewardPolicyApplicableTargetEntity> entities = new ArrayList<>();
        for (var target : targets) {
            RewardPolicyApplicableTargetEntity entity = RewardPolicyApplicableTargetEntity.builder()
                    .rewardPolicyId(policyId)
                    .targetId(target.getTarget().getId())
                    .build();
            entities.add(entity);
        }
        return entities;
    }

    public Set<Long> getTargetIdsByPolicyId(Long policyId) {
        return targetRepository.getTargetIdsByPolicyId(policyId);
    }
}

