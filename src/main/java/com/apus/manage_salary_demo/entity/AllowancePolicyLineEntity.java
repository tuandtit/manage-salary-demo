package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "allowance_policy_line")
@Getter
@Setter
public class AllowancePolicyLineEntity extends AbstractAuditingEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allowance_policy_id", nullable = false)
    private AllowancePolicyEntity allowancePolicyEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allowance_id", nullable = false)
    private AllowanceEntity allowanceEntity;

    @Column(nullable = false, length = 50)
    private String cycle;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}