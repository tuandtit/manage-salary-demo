package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.Cycle;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "allowance_policy_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowancePolicyLineEntity extends AbstractAuditingEntity<Long> {

    private Long allowancePolicyId;

    private Long allowanceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Cycle cycle = Cycle.MONTHLY;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
}