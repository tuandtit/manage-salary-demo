package com.apus.manage_salary_demo.client.resources.dto;

import com.apus.manage_salary_demo.dto.SimpleDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto {
    private Long id;
    private String code;
    private String name;
    private SimpleDto department;
    private SimpleDto position;
}
