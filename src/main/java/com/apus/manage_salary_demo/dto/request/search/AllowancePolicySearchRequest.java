package com.apus.manage_salary_demo.dto.request.search;

import com.apus.manage_salary_demo.common.enums.AllowancePolicyState;
import com.apus.manage_salary_demo.common.enums.ApplicableType;
import com.apus.manage_salary_demo.common.enums.PolicyType;
import com.apus.manage_salary_demo.dto.request.FilterRequest;
import com.apus.manage_salary_demo.dto.validator.EnumValueOrList;
import com.apus.manage_salary_demo.entity.AllowancePolicyEntity;
import com.apus.manage_salary_demo.repository.specifiation.AllowancePolicySpecification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AllowancePolicySearchRequest extends FilterRequest<AllowancePolicyEntity> {
    List<Long> ids;
    String search;

    @EnumValueOrList(name = "type", enumClass = PolicyType.class)
    String type;

    LocalDate startDate;

    LocalDate endDate;

    @EnumValueOrList(name = "applicableType", enumClass = ApplicableType.class)
    String applicableType;

    @EnumValueOrList(name = "state", enumClass = AllowancePolicyState.class)
    String state;

    @Override
    public Specification<AllowancePolicyEntity> specification() {
        return AllowancePolicySpecification.builder()
                .withCodeOrName(search)
                .withTarget(applicableType)
                .withType(type)
                .withStartDate(startDate)
                .withEndDate(endDate)
                .withState(state)
                .withIds(ids)
                .build();
    }
}
