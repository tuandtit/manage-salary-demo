package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "allowance_policy_line")
@Getter
@Setter
public class AllowancePolicyLine extends AbstractAuditingEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allowance_policy_id", nullable = false)
    private AllowancePolicy allowancePolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allowance_id", nullable = false)
    private Allowance allowance;

    @Column(nullable = false, length = 50)
    private String cycle;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}