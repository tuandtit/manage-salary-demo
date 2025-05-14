package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "allowance_policy_applicable_target")
@Getter
@Setter
public class AllowancePolicyApplicableTarget extends AbstractAuditingEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allowance_policy_id", nullable = false)
    private AllowancePolicy allowancePolicy;

    @Column(name = "target_id")
    private Long targetId;
}