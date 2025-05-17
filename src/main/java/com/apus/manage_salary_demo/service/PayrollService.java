package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.PayrollDto;
import com.apus.manage_salary_demo.dto.request.PayrollRequest;
import com.apus.manage_salary_demo.dto.request.search.PayrollSearchRequest;
import org.springframework.data.domain.Page;

public interface PayrollService {
    BaseDto create(PayrollRequest dto);

    BaseDto update(PayrollDto dto);

    void delete(Long id);

    PayrollDto getById(Long id);

    Page<PayrollDto> getAll(PayrollSearchRequest request);
}
