package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.GroupAllowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupAllowanceRepository extends JpaRepository<GroupAllowance, Long>, JpaSpecificationExecutor<GroupAllowance> {
    boolean existsByCode(String code);
}
