package com.apus.manage_salary_demo.dto.response;

import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.dto.RewardDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayrollRewardLineResponse {
    private Long id;
    private SimpleDto groupReward;
    private RewardDto reward;
    private String amountItem;
    private BigDecimal amount;
    private BigDecimal taxableAmount;
    private BigDecimal insuranceAmount;
}