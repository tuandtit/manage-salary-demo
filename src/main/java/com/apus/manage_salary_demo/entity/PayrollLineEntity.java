package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payroll_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payroll_id")
    private PayrollEntity payrollEntity;

    @Column(name = "group_target_id", nullable = false)
    private Long groupTargetId;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(length = 50, nullable = false)
    private String amountItem;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal taxableAmount = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal insuranceAmount = BigDecimal.ZERO;
}
