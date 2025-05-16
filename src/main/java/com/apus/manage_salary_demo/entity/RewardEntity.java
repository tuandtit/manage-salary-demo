package com.apus.manage_salary_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "reward")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardEntity extends AbstractAuditingEntity<Long> {

    @Column(length = 20, nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private Long groupRewardId;

    @Column(length = 50)
    private String includeType;

    @Column(length = 50, nullable = false)
    private String type;

    @Column(nullable = false)
    private Long uomId;

    @Column(nullable = false)
    private Long currencyId;

    @Column(columnDefinition = "text")
    private String description;
}
