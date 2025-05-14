package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reward_policy_applicable_target")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardPolicyApplicableTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reward_policy_id")
    private RewardPolicy rewardPolicy;

    private Long targetId;
}
