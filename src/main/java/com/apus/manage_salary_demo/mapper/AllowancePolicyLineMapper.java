package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.AllowancePolicyLineDto;
import com.apus.manage_salary_demo.entity.AllowancePolicyLineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface AllowancePolicyLineMapper extends EntityMapper<AllowancePolicyLineDto, AllowancePolicyLineEntity> {
}
