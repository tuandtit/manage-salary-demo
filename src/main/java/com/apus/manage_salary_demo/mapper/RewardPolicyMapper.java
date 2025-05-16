package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.RewardPolicyDto;
import com.apus.manage_salary_demo.entity.RewardPolicyEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface RewardPolicyMapper extends EntityMapper<RewardPolicyDto, RewardPolicyEntity> {

}
