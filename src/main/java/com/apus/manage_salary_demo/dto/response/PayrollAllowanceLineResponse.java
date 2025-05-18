package com.apus.manage_salary_demo.dto.response;

import com.apus.manage_salary_demo.dto.SimpleDto;
import com.apus.manage_salary_demo.dto.AllowanceDto;
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
public class PayrollAllowanceLineResponse {
    private Long id;
    private SimpleDto groupAllowance;
    private AllowanceDto allowance;
    private String amountItem;
    private BigDecimal amount;
    private BigDecimal taxableAmount;
    private BigDecimal insuranceAmount;
}