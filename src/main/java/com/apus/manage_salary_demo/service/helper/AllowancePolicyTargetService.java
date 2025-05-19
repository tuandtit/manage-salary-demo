package com.apus.manage_salary_demo.service.helper;

import com.apus.manage_salary_demo.dto.ApplicableTargetDto;
import com.apus.manage_salary_demo.entity.AllowancePolicyApplicableTargetEntity;
import com.apus.manage_salary_demo.repository.AllowancePolicyTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AllowancePolicyTargetService {

    private final AllowancePolicyTargetRepository targetRepository;

    @Transactional
    public void saveTargets(Long policyId, List<ApplicableTargetDto> targets) {
        if (policyId == null || targets == null || targets.isEmpty()) return;

        List<AllowancePolicyApplicableTargetEntity> entities = buildTargets(policyId, targets);

        targetRepository.saveAll(entities);
    }

    public void deleteByPolicyId(Long policyId) {
        targetRepository.deleteByAllowancePolicyId(policyId);
    }

    @Transactional
    public void updateTargets(Long policyId, List<ApplicableTargetDto> incomingDtos) {
        if (policyId == null) return;

        // Xóa toàn bộ target hiện tại của policy
        targetRepository.deleteByAllowancePolicyId(policyId);

        // Nếu danh sách mới không rỗng thì tạo lại
        if (incomingDtos != null && !incomingDtos.isEmpty()) {
            List<AllowancePolicyApplicableTargetEntity> newEntities = buildTargets(policyId, incomingDtos);
            targetRepository.saveAll(newEntities);
        }
    }

    private List<AllowancePolicyApplicableTargetEntity> buildTargets(Long policyId, List<ApplicableTargetDto> targets) {
        List<AllowancePolicyApplicableTargetEntity> entities = new ArrayList<>();
        for (var target : targets) {
            AllowancePolicyApplicableTargetEntity entity = AllowancePolicyApplicableTargetEntity.builder()
                    .allowancePolicyId(policyId)
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

