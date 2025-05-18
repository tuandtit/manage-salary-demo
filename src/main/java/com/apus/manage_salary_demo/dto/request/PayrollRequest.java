package com.apus.manage_salary_demo.dto.request;

import com.apus.manage_salary_demo.dto.SimpleDto;
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

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayrollRequest {
    private Long id;
    @Valid
    private SimpleDto employee;
    @Valid
    private SimpleDto department;
    @Valid
    private SimpleDto position;

    @NotNull(message = "type must be not null")
    @EnumValueOrList(name = "type", enumClass = PayrollType.class)
    private String type;

    @NotNull(message = "cycle must be not null")
    @EnumValueOrList(name = "cycle", enumClass = Cycle.class)
    private String cycle;

    @NotNull(message = "startDate must be not null")
    private LocalDate startDate;
    private String note;

    @Valid
    private List<PayrollAllowanceLineRequest> allowances;

    @Valid
    private List<PayrollRewardLineRequest> rewards;
}
