package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollEntity, Long>, JpaSpecificationExecutor<PayrollEntity> {
}
