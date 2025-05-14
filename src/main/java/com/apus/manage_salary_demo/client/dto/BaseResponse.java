package com.apus.manage_salary_demo.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private String message;
    private String traceId;
    private T data;
}
