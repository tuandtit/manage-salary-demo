package com.apus.manage_salary_demo.service;

import com.apus.manage_salary_demo.dto.BaseDto;
import com.apus.manage_salary_demo.dto.RewardPolicyDto;
import com.apus.manage_salary_demo.dto.request.search.RewardPolicySearchRequest;
import org.springframework.data.domain.Page;

public interface RewardPolicyService {
    BaseDto create(RewardPolicyDto dto);

    BaseDto update(RewardPolicyDto dto);

    void delete(Long id);

    RewardPolicyDto getById(Long id);

    Page<RewardPolicyDto> getAll(RewardPolicySearchRequest request);
}
