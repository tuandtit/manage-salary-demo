package com.apus.manage_salary_demo.client.resources.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CurrencySearchRequest {
    List<Long> currencyIds;
}
