package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.PayrollDto;
import com.apus.manage_salary_demo.entity.PayrollEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface PayrollMapper extends EntityMapper<PayrollDto, PayrollEntity> {
    @Override
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "positionId", source = "position.id")
    PayrollEntity toEntity(PayrollDto request);
}
