package com.apus.manage_salary_demo.common.constant;

import com.apus.manage_salary_demo.common.error.MessageCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppConstant {
    public static final MessageCode SERVICE_ERROR = new MessageCode(HttpStatus.INTERNAL_SERVER_ERROR);
    public static final MessageCode BAD_REQUEST = new MessageCode(HttpStatus.BAD_REQUEST);
    public static final String SYSTEM = "system";
}
