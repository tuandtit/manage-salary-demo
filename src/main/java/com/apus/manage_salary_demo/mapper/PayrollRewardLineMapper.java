package com.apus.manage_salary_demo.mapper;

import com.apus.manage_salary_demo.dto.request.PayrollRewardLineRequest;
import com.apus.manage_salary_demo.dto.response.PayrollRewardLineResponse;
import com.apus.manage_salary_demo.entity.PayrollRewardLineEntity;
import org.mapstruct.*;

@Mapper(
        config = DefaultConfigMapper.class
)
public interface PayrollRewardLineMapper extends EntityMapper<PayrollRewardLineResponse, PayrollRewardLineEntity> {

    PayrollRewardLineEntity toEntity(PayrollRewardLineRequest dto);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void update(PayrollRewardLineRequest dto, @MappingTarget PayrollRewardLineEntity entity);
}
