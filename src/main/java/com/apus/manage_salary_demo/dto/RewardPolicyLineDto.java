package com.apus.manage_salary_demo.dto;

import com.apus.manage_salary_demo.common.enums.Cycle;
import com.apus.manage_salary_demo.dto.validator.EnumValueOrList;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
public class RewardPolicyLineDto {

    private Long id;

    private RewardDto reward;

    @NotNull(message = "cycle must be not null")
    @EnumValueOrList(name = "cycle", enumClass = Cycle.class)
    private String cycle;

    @NotNull(message = "amount must be not null")
    private BigDecimal amount;
}
