package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.RewardDto;
import com.apus.manage_salary_demo.dto.request.search.RewardSearchRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface RewardService {

    BaseDto create(RewardDto dto);

    BaseDto update(RewardDto dto);

    void delete(Long id);

    RewardDto getById(Long id);

    Page<RewardDto> getAll(RewardSearchRequest request);

    List<RewardDto> getAllDetailByIds(Set<Long> ids);
}
