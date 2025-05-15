package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.request.search.AllowanceSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface AllowanceService {

    BaseDto create(AllowanceDto dto);

    BaseDto update(AllowanceDto dto);

    void delete(Long id);

    AllowanceDto getById(Long id);

    Page<AllowanceDto> getAll(AllowanceSearchRequest request);

    List<AllowanceDto> getAllDetailByIds(Set<Long> ids);
}
