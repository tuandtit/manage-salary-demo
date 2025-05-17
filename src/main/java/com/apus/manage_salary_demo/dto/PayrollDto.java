package com.apus.manage_salary_demo.dto;

import com.apus.manage_salary_demo.client.resources.dto.TargetDto;
import com.apus.manage_salary_demo.common.enums.Cycle;
import com.apus.manage_salary_demo.common.enums.PayrollType;
import com.apus.manage_salary_demo.dto.validator.EnumValueOrList;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @Valid
    private TargetDto employee;
    @Valid
    private TargetDto department;
    @Valid
    private TargetDto position;

    @NotNull(message = "type must be not null")
    @EnumValueOrList(name = "type", enumClass = PayrollType.class)
    private String type;

    @NotNull(message = "cycle must be not null")
    @EnumValueOrList(name = "cycle", enumClass = Cycle.class)
    private String cycle;

    private BigDecimal totalAllowanceAmount;

    @NotNull(message = "startDate must be not null")
    private String startDate;
    private String note;

    @Valid
    private List<PayrollAllowanceLineDto> allowances;

    @Valid
    private List<PayrollRewardLineDto> rewards;
}
