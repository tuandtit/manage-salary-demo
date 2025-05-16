package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.RewardPolicyLineDto;
import com.apus.manage_salary_demo.entity.RewardPolicyLineEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface RewardPolicyLineMapper extends EntityMapper<RewardPolicyLineDto, RewardPolicyLineEntity> {
}
