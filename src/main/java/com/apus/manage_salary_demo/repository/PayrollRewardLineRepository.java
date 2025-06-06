package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.PayrollRewardLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayrollRewardLineRepository extends JpaRepository<PayrollRewardLineEntity, Long> {
    List<PayrollRewardLineEntity> findByPayrollId(Long payrollId);
    List<PayrollRewardLineEntity> findByPayrollIdAndGroupRewardId(Long payrollId, Long groupRewardId);

    void deleteByPayrollId(Long payrollId);
}
