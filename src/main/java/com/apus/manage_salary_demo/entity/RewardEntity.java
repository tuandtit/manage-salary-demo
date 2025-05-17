package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.AllowanceRewardType;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private AllowanceRewardType type;

    @Column(nullable = false)
    private Long uomId;

    @Column(nullable = false)
    private Long currencyId;

    @Column(columnDefinition = "text")
    private String description;
}
