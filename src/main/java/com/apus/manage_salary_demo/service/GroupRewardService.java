package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.GroupRewardDto;
import com.apus.manage_salary_demo.dto.request.search.GroupRewardSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface GroupRewardService {

    BaseDto create(GroupRewardDto dto);

    BaseDto update(GroupRewardDto dto);

    void delete(Long id);

    GroupRewardDto getById(Long id);

    Page<GroupRewardDto> getAll(GroupRewardSearchRequest request);

    List<GroupRewardDto> getAllDetailByIds(Set<Long> ids);
}
