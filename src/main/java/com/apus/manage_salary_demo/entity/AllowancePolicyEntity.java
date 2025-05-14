package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "allowance_policy")
@Getter
@Setter
public class AllowancePolicyEntity extends AbstractAuditingEntity<Long> {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "applicable_type", nullable = false, length = 50)
    private String applicableType;

    @Column(nullable = false, length = 50)
    private String state;

    @OneToMany(mappedBy = "allowancePolicyEntity", cascade = CascadeType.ALL)
    private List<AllowancePolicyLineEntity> lines;

    @OneToMany(mappedBy = "allowancePolicyEntity", cascade = CascadeType.ALL)
    private List<AllowancePolicyApplicableTargetEntity> targets;
}
