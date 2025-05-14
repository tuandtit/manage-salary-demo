package com.apus.manage_salary_demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "reward_policy_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardPolicyLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reward_policy_id")
    private RewardPolicy rewardPolicy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @Column(length = 50, nullable = false)
    private String cycle;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;
}
