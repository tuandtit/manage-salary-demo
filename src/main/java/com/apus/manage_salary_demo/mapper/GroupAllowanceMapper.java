package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.GroupAllowanceDto;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import org.mapstruct.*;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface GroupAllowanceMapper extends EntityMapper<GroupAllowanceDto, GroupAllowanceEntity> {
}
