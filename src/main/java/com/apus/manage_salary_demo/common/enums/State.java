package com.apus.manage_salary_demo.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum State {
    //dùng chung
    DRAFT, //dự thảo
    VALID,
    INVALID,
    CANCELLED,

    //dùng riêng cho màn reward;
    PUBLISH,
    STORE,
}
