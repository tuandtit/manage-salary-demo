package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.AllowancePolicyLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowancePolicyLineRepository extends JpaRepository<AllowancePolicyLineEntity, Long> {
    List<AllowancePolicyLineEntity> findByAllowancePolicyId(Long policyId);
}
