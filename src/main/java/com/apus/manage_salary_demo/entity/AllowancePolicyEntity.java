package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.AllowancePolicyState;
import com.apus.manage_salary_demo.common.enums.ApplicableType;
import com.apus.manage_salary_demo.common.enums.PolicyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "allowance_policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowancePolicyEntity extends AbstractAuditingEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private PolicyType type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "applicable_type", nullable = false, length = 50)
    private ApplicableType applicableType = ApplicableType.ALL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AllowancePolicyState state = AllowancePolicyState.DRAFT;
}
