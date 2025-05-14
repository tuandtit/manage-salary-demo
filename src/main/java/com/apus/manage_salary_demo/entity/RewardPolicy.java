package com.apus.manage_salary_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reward_policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardPolicy extends AbstractAuditingEntity<Long> {

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

    @Column(length = 50, nullable = false, unique = true)
    private String applicableType;
}
