package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupAllowanceRepository extends JpaRepository<GroupAllowanceEntity, Long>, JpaSpecificationExecutor<GroupAllowanceEntity> {
    boolean existsByCode(String code);
}
