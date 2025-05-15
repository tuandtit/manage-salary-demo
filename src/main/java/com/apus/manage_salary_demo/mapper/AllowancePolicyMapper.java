package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.AllowancePolicyDto;
import com.apus.manage_salary_demo.entity.AllowancePolicyEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface AllowancePolicyMapper extends EntityMapper<AllowancePolicyDto, AllowancePolicyEntity> {

}
