package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollAllowanceLineRepository extends JpaRepository<PayrollAllowanceLineEntity, Long> {
    List<PayrollAllowanceLineEntity> findByPayrollIdAndGroupAllowanceId(Long payrollId, Long allowanceId);

    void deleteByPayrollId(Long payrollId);

    List<PayrollAllowanceLineEntity> findByPayrollId(Long payrollId);
}
