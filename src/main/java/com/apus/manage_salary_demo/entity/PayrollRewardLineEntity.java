package com.apus.manage_salary_demo.entity;

import com.apus.manage_salary_demo.common.enums.AmountItem;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payroll_reward_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRewardLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long payrollId;

    private Long groupRewardId;

    private Long rewardId;

    @Enumerated(EnumType.STRING)
    private AmountItem amountItem;

    private BigDecimal amount = BigDecimal.ZERO;

    private BigDecimal taxableAmount = BigDecimal.ZERO;

    private BigDecimal insuranceAmount = BigDecimal.ZERO;
}
