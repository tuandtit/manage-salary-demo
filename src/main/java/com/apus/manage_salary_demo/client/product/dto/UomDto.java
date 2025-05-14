package com.apus.manage_salary_demo.client.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UomDto {
    private Long id;
    private String code;
    private String name;
}
