package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.AllowanceLineDto;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface AllowanceLineMapper extends EntityMapper<AllowanceLineDto, PayrollAllowanceLineEntity> {
    @Override
    @Mapping(target = "allowanceId", source = "allowance.id")
    PayrollAllowanceLineEntity toEntity(AllowanceLineDto request);
}
