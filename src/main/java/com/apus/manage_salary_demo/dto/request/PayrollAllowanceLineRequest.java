package com.apus.manage_salary_demo.dto.request;

import com.apus.manage_salary_demo.common.enums.AmountItem;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.validator.EnumValueOrList;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
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
public class PayrollAllowanceLineRequest {
    @Valid
    private BaseDto groupAllowance;
    @Valid
    private BaseDto allowance;

    @NotNull(message = "amountItem must be not null")
    @EnumValueOrList(name = "amountItem", enumClass = AmountItem.class)
    private String amountItem;

    private BigDecimal amount;
    private BigDecimal taxableAmount;
    private BigDecimal insuranceAmount;
}