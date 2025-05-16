package com.apus.manage_salary_demo.dto;

import com.apus.manage_salary_demo.common.enums.ApplicableType;
import com.apus.manage_salary_demo.common.enums.PolicyType;
import com.apus.manage_salary_demo.common.enums.RewardPolicyState;
import com.apus.manage_salary_demo.dto.validator.EnumValueOrList;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class RewardPolicyDto {
    private Long id;

    @NotBlank(message = "Code must not be blank")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotNull(message = "type must be not null")
    @EnumValueOrList(name = "type", enumClass = PolicyType.class)
    private String type;

    @NotNull(message = "startDate must be not null")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "applicableType must be not null")
    @EnumValueOrList(name = "applicableType", enumClass = ApplicableType.class)
    private String applicableType;

    @NotNull(message = "state must be not null")
    @EnumValueOrList(name = "state", enumClass = RewardPolicyState.class)
    private String state;

    private List<ApplicableTargetDto> targets;

    @Valid
    private List<RewardPolicyLineDto> lines;

    private String description;
}
