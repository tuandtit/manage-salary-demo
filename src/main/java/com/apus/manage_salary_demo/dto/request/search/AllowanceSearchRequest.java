package com.apus.manage_salary_demo.dto.request.search;

import com.apus.manage_salary_demo.dto.request.FilterRequest;
import com.apus.manage_salary_demo.entity.Allowance;
import com.apus.manage_salary_demo.repository.specifiation.AllowanceSpecification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AllowanceSearchRequest extends FilterRequest<Allowance> {
    List<Long> ids;
    String search;
    Boolean isActive;

    @Override
    public Specification<Allowance> specification() {
        return AllowanceSpecification.builder()
                .withIds(ids)
                .withCodeOrName(search)
                .withActive(isActive)
                .build();
    }
}
