package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.Cycle;
import com.apus.manage_salary_demo.common.enums.PayrollType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payroll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollEntity extends AbstractAuditingEntity<Long> {

    private Long employeeId;

    private Long departmentId;

    private Long positionId;

    @Enumerated(EnumType.STRING)
    private PayrollType type;

    @Enumerated(EnumType.STRING)
    private Cycle cycle;

    private LocalDate startDate;

    private BigDecimal totalAllowanceAmount;

    private String note;
}
