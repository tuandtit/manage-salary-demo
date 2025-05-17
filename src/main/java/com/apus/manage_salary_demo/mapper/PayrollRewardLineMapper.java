package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.PayrollRewardLineDto;
import com.apus.manage_salary_demo.dto.request.PayrollRewardLineRequest;
import com.apus.manage_salary_demo.entity.PayrollRewardLineEntity;
import org.mapstruct.Mapper;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface PayrollRewardLineMapper extends EntityMapper<PayrollRewardLineDto, PayrollRewardLineEntity> {

    PayrollRewardLineEntity toEntity(PayrollRewardLineRequest dto);
}
