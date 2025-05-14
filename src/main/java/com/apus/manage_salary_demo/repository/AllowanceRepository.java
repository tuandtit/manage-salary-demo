package com.apus.manage_salary_demo.repository;

import com.apus.manage_salary_demo.entity.Allowance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AllowanceRepository extends JpaRepository<Allowance, Long>, JpaSpecificationExecutor<Allowance> {
    boolean existsByCode(String code);

    @Query("SELECT DISTINCT a.uomId FROM Allowance a")
    List<Long> findDistinctUomIds();

    @Query("SELECT DISTINCT a.uomId FROM Allowance a")
    List<Long> findDistinctCurrencyIds();
}
