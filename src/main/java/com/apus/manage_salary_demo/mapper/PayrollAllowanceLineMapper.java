package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.AllowanceLineDto;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface PayrollAllowanceLineMapper extends EntityMapper<AllowanceLineDto, PayrollAllowanceLineEntity> {
}
