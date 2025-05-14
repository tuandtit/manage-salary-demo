package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.AllowanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AllowanceRepository extends JpaRepository<AllowanceEntity, Long>, JpaSpecificationExecutor<AllowanceEntity> {
    boolean existsByCode(String code);
}
