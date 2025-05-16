package com.apus.manage_salary_demo.dto;

import com.apus.manage_salary_demo.client.product.dto.UomDto;
import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.apus.manage_salary_demo.common.enums.AllowanceRewardType;
import com.apus.manage_salary_demo.common.enums.DeductionType;
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

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardDto {
    private Long id;

    @NotBlank(message = "Code must not be blank")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @EnumValueOrList(name = "includeTypes", enumClass = DeductionType.class)
    List<String> includeTypes;

    @NotNull(message = "groupAllowance must be not null")
    private BaseDto groupReward;

    @Valid
    private UomDto uom;

    @Valid
    private CurrencyDto currency;

    @NotNull(message = "type must be not null")
    @EnumValueOrList(name = "type", enumClass = AllowanceRewardType.class)
    private String type;

    private String description;
}
