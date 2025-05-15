package com.apus.manage_salary_demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AllowancePolicyState {
    DRAFT, //dự thảo
    VALID,
    INVALID,
    CANCELLED,
}
