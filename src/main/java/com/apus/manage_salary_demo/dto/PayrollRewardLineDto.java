package com.apus.manage_salary_demo.dto;

import com.apus.manage_salary_demo.client.resources.dto.TargetDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayrollRewardLineDto {
    private TargetDto groupAllowance;
    private List<RewardLineDto> rewardLines;
}