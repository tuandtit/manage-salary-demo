package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.RewardPolicyApplicableTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RewardPolicyTargetRepository extends JpaRepository<RewardPolicyApplicableTargetEntity, Long> {
    List<RewardPolicyApplicableTargetEntity> findByRewardPolicyId(Long policyId);

    @Query("SELECT t.targetId FROM RewardPolicyApplicableTargetEntity t WHERE t.rewardPolicyId = :policyId")
    Set<Long> getTargetIdsByPolicyId(Long policyId);

    void deleteByRewardPolicyId(Long policyId);
}
