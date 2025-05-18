package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.AllowanceDto;
import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.GroupAllowanceDto;
import com.apus.manage_salary_demo.dto.request.search.GroupAllowanceSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface GroupAllowanceService {

    BaseDto create(GroupAllowanceDto dto);

    BaseDto update(GroupAllowanceDto dto);

    void delete(Long id);

    GroupAllowanceDto getById(Long id);

    Page<GroupAllowanceDto> getAll(GroupAllowanceSearchRequest request);

    List<GroupAllowanceDto> getAllDetailByIds(Set<Long> ids);
}
