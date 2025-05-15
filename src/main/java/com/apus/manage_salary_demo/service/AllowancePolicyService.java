package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.AllowancePolicyDto;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.request.search.AllowancePolicySearchRequest;
import org.springframework.data.domain.Page;

public interface AllowancePolicyService {
    BaseDto create(AllowancePolicyDto dto);

    BaseDto update(AllowancePolicyDto dto);

    void delete(Long id);

    AllowancePolicyDto getById(Long id);

    Page<AllowancePolicyDto> getAll(AllowancePolicySearchRequest request);
}
