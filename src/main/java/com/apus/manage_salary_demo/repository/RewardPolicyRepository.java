package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.RewardPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardPolicyRepository extends JpaRepository<RewardPolicyEntity, Long>, JpaSpecificationExecutor<RewardPolicyEntity> {
    boolean existsByCode(String code);
}
