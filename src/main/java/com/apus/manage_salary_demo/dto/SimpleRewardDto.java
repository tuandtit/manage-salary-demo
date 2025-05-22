package com.apus.manage_salary_demo.dto;

import com.apus.manage_salary_demo.client.resources.dto.CurrencyDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
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
public class SimpleRewardDto {
    private Long id;
    private String code;
    private String name;
    List<String> includeTypes;
    @Valid
    private CurrencyDto currency;
}
