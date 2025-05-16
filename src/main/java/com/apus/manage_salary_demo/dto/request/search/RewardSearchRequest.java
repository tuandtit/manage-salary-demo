package com.apus.manage_salary_demo.dto.request.search;

import com.apus.manage_salary_demo.common.enums.AllowanceRewardType;
import com.apus.manage_salary_demo.dto.request.FilterRequest;
import com.apus.manage_salary_demo.dto.validator.EnumValueOrList;
import com.apus.manage_salary_demo.entity.RewardEntity;
import com.apus.manage_salary_demo.repository.specifiation.RewardSpecification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RewardSearchRequest extends FilterRequest<RewardEntity> {
    List<Long> ids;
    String search;
    @EnumValueOrList(name = "type", enumClass = AllowanceRewardType.class)
    private String type;

    @Override
    public Specification<RewardEntity> specification() {
        return RewardSpecification.builder()
                .withType(type)
                .withCodeOrName(search)
                .withIds(ids)
                .build();
    }
}
