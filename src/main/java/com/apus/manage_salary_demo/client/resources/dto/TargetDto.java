package com.apus.manage_salary_demo.client.resources.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TargetDto {
    @NotNull(message = "id must be not null")
    private Long id;
    private String code;
    private String name;
}
