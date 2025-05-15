package com.apus.manage_salary_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "allowance_policy_applicable_target")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowancePolicyApplicableTargetEntity extends AbstractAuditingEntity<Long> {
    @Column(name = "allowance_policy_id")
    private Long allowancePolicyId;

    @Column(name = "target_id")
    private Long targetId;
}