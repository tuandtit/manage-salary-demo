package com.apus.manage_salary_demo.repository.specifiation;

import com.apus.manage_salary_demo.entity.AbstractAuditingEntity_;
import com.apus.manage_salary_demo.entity.PayrollEntity;
import com.apus.manage_salary_demo.entity.PayrollEntity_;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PayrollSpecification {
    private final List<Specification<PayrollEntity>> specifications = new ArrayList<>();

    public static PayrollSpecification builder() {
        return new PayrollSpecification();
    }

    public PayrollSpecification withEmployeeIds(List<Long> employeeIds) {
        if (!ObjectUtils.isEmpty(employeeIds)) {

            specifications.add(
                    (root, q, cb) ->
                            root.get(PayrollEntity_.EMPLOYEE_ID).in(employeeIds)
            );
        }
        return this;
    }

    public PayrollSpecification withDepartmentId(Long departmentId) {
        if (departmentId != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(PayrollEntity_.DEPARTMENT_ID), departmentId)
            );
        }
        return this;
    }

    public PayrollSpecification withPositionId(Long positionId) {
        if (positionId != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(PayrollEntity_.POSITION_ID), positionId)
            );
        }
        return this;
    }

    public PayrollSpecification withCycle(String cycle) {
        if (cycle != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(PayrollEntity_.CYCLE), cycle)
            );
        }
        return this;
    }


    public PayrollSpecification withIds(List<Long> ids) {
        if (!ObjectUtils.isEmpty(ids)) {
            specifications.add(
                    (root, query, criteriaBuilder) ->
                            root.get(AbstractAuditingEntity_.id).in(ids)
            );
        }
        return this;
    }

    public Specification<PayrollEntity> build() {
        return (root, q, cb) -> cb.and(specifications.stream()
                .filter(Objects::nonNull)
                .map(s -> s.toPredicate(root, q, cb)).toArray(Predicate[]::new));
    }

}
