package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.RewardPolicyLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardPolicyLineRepository extends JpaRepository<RewardPolicyLineEntity, Long> {
    List<RewardPolicyLineEntity> findByRewardPolicyId(Long policyId);

    void deleteByRewardPolicyId(Long policyId);
}
