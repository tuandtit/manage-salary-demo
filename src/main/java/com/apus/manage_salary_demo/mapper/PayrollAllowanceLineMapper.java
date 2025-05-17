package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.PayrollAllowanceLineDto;
import com.apus.manage_salary_demo.dto.request.PayrollAllowanceLineRequest;
import com.apus.manage_salary_demo.entity.PayrollAllowanceLineEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class,
        uses = {AllowanceLineMapper.class}
)
public interface PayrollAllowanceLineMapper extends EntityMapper<PayrollAllowanceLineDto, PayrollAllowanceLineEntity> {

    PayrollAllowanceLineEntity toEntity(PayrollAllowanceLineRequest dto);
}
