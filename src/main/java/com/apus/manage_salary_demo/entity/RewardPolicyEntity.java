package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.ApplicableType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reward_policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardPolicyEntity extends AbstractAuditingEntity<Long> {

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 50, nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = 50, nullable = false)
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private ApplicableType applicableType = ApplicableType.ALL;
}
