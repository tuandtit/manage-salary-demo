package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.GroupRewardDto;
import com.apus.manage_salary_demo.entity.GroupRewardEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface GroupRewardMapper extends EntityMapper<GroupRewardDto, GroupRewardEntity> {
}
