package com.apus.manage_salary_demo.entity;

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

    @Column(length = 255, nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_reward_id")
    private GroupRewardEntity groupRewardEntity;

    @Column(length = 50, nullable = false)
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
