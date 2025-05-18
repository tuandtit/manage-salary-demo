package com.apus.manage_salary_demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayrollDto {
    private Long id;
    private SimpleDto employee;
    private SimpleDto department;
    private SimpleDto position;
    private String type;
    private String cycle;
    private BigDecimal totalAllowanceAmount;
    private String startDate;
    private String note;
    private List<PayrollAllowanceLineDto> allowances;
    private List<PayrollRewardLineDto> rewards;
}
