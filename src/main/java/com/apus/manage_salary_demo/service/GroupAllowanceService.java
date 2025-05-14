package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.GroupAllowanceDto;
import com.apus.manage_salary_demo.dto.request.search.GroupAllowanceSearchRequest;
import org.springframework.data.domain.Page;

public interface GroupAllowanceService {

    GroupAllowanceDto create(GroupAllowanceDto dto);

    GroupAllowanceDto update(GroupAllowanceDto dto);

    void delete(Long id);

    GroupAllowanceDto getById(Long id);

    Page<GroupAllowanceDto> getAll(GroupAllowanceSearchRequest request);
}
