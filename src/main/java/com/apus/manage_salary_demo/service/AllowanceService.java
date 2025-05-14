package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.request.search.AllowanceSearchRequest;
import org.springframework.data.domain.Page;

public interface AllowanceService {

    AllowanceDto create(AllowanceDto dto);

    AllowanceDto update(AllowanceDto dto);

    void delete(Long id);

    AllowanceDto getById(Long id);

    Page<AllowanceDto> getAll(AllowanceSearchRequest request);
}
