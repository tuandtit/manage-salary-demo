package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.AllowancePolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowancePolicyRepository extends JpaRepository<AllowancePolicyEntity, Long>, JpaSpecificationExecutor<AllowancePolicyEntity> {
    boolean existsByCode(String code);
}
