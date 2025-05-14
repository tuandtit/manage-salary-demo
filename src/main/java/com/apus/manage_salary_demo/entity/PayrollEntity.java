package com.apus.manage_salary_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Column(length = 50, nullable = false)
    private String type;

    @Column(length = 50, nullable = false)
    private String cycle;

    private LocalDate startDate;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAllowanceAmount;

    @Column(columnDefinition = "text")
    private String note;
}
