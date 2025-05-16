package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.AllowancePolicyApplicableTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface AllowancePolicyTargetRepository extends JpaRepository<AllowancePolicyApplicableTargetEntity, Long> {
    List<AllowancePolicyApplicableTargetEntity> findByAllowancePolicyId(Long policyId);

    @Query("SELECT t.targetId FROM AllowancePolicyApplicableTargetEntity t WHERE t.allowancePolicyId = :policyId")
    Set<Long> getTargetIdsByPolicyId(Long policyId);

    void deleteByAllowancePolicyId(Long policyId);
}
