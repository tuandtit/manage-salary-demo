package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.request.PayrollAllowanceLineRequest;
import com.apus.manage_salary_demo.dto.response.PayrollAllowanceLineResponse;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import org.mapstruct.*;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface PayrollAllowanceLineMapper extends EntityMapper<PayrollAllowanceLineResponse, PayrollAllowanceLineEntity> {

    PayrollAllowanceLineEntity toEntity(PayrollAllowanceLineRequest dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void update(PayrollAllowanceLineRequest dto, @MappingTarget PayrollAllowanceLineEntity entity);
}
