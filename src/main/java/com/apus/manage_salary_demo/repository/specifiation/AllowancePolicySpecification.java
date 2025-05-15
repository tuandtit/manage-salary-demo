package com.apus.manage_salary_demo.repository.specifiation;

import com.apus.manage_salary_demo.common.enums.AllowancePolicyState;
import com.apus.manage_salary_demo.common.enums.AllowanceRewardType;
import com.apus.manage_salary_demo.common.enums.ApplicableType;
import com.apus.manage_salary_demo.common.utils.TextUtils;
import com.apus.manage_salary_demo.entity.AbstractAuditingEntity_;
import com.apus.manage_salary_demo.entity.AllowancePolicyEntity;
import com.apus.manage_salary_demo.entity.AllowancePolicyEntity_;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AllowancePolicySpecification {
    private final List<Specification<AllowancePolicyEntity>> specifications = new ArrayList<>();

    public static AllowancePolicySpecification builder() {
        return new AllowancePolicySpecification();
    }

    public AllowancePolicySpecification withCodeOrName(final String search) {
        if (!ObjectUtils.isEmpty(search)) {
            specifications.add(
                    (root, q, cb) -> {
                        String likePattern = TextUtils.like(search);
                        // Tìm kiếm keyword nằm trong code hoặc name
                        // Sử dụng unaccent của postgresql để loại bỏ tiếng việt
                        return cb.or(
                                cb.like(
                                        cb.lower(cb.function("unaccent", String.class, root.get(AllowancePolicyEntity_.NAME))),
                                        likePattern
                                ),
                                cb.like(
                                        cb.lower(cb.function("unaccent", String.class, root.get(AllowancePolicyEntity_.CODE))),
                                        likePattern
                                )
                        );
                    }
            );
        }
        return this;
    }

    public AllowancePolicySpecification withState(String state) {
        if (state != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(AllowancePolicyEntity_.STATE), state)
            );
        }
        return this;
    }

    public AllowancePolicySpecification withTarget(String applicableType) {
        if (applicableType != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(AllowancePolicyEntity_.APPLICABLE_TYPE), applicableType)
            );
        }
        return this;
    }

    public AllowancePolicySpecification withType(String type) {
        if (type != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(AllowancePolicyEntity_.TYPE), type)
            );
        }
        return this;
    }

    public AllowancePolicySpecification withStartDate(LocalDate startDate) {
        if (startDate != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(AllowancePolicyEntity_.START_DATE), startDate)
            );
        }
        return this;
    }

    public AllowancePolicySpecification withEndDate(LocalDate endDate) {
        if (endDate != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(AllowancePolicyEntity_.END_DATE), endDate)
            );
        }
        return this;
    }

    public AllowancePolicySpecification withIds(List<Long> ids) {
        if (!ObjectUtils.isEmpty(ids)) {
            specifications.add(
                    (root, query, criteriaBuilder) ->
                            root.get(AbstractAuditingEntity_.id).in(ids)
            );
        }
        return this;
    }

    public Specification<AllowancePolicyEntity> build() {
        return (root, q, cb) -> cb.and(specifications.stream()
                .filter(Objects::nonNull)
                .map(s -> s.toPredicate(root, q, cb)).toArray(Predicate[]::new));
    }

}
