package com.apus.manage_salary_demo.dto.request.search;

import com.apus.manage_salary_demo.common.enums.Cycle;
import com.apus.manage_salary_demo.dto.request.FilterRequest;
import com.apus.manage_salary_demo.dto.validator.EnumValueOrList;
import com.apus.manage_salary_demo.entity.PayrollEntity;
import com.apus.manage_salary_demo.repository.specifiation.PayrollSpecification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayrollSearchRequest extends FilterRequest<PayrollEntity> {
    List<Long> ids;
    List<Long> employeeIds;
    Long departmentId;
    Long positionId;
    @EnumValueOrList(name = "cycle", enumClass = Cycle.class)
    String cycle;

    @Override
    public Specification<PayrollEntity> specification() {
        return PayrollSpecification.builder()
                .withEmployeeIds(employeeIds)
                .withDepartmentId(departmentId)
                .withPositionId(positionId)
                .withCycle(cycle)
                .withIds(ids)
                .build();
    }
}
