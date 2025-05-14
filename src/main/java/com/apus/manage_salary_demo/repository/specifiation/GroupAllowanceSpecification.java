package com.apus.manage_salary_demo.repository.specifiation;

import com.apus.manage_salary_demo.common.utils.TextUtils;
import com.apus.manage_salary_demo.entity.AbstractAuditingEntity_;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity;
import com.apus.manage_salary_demo.entity.GroupAllowanceEntity_;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GroupAllowanceSpecification {
    private final List<Specification<GroupAllowanceEntity>> specifications = new ArrayList<>();

    public static GroupAllowanceSpecification builder() {
        return new GroupAllowanceSpecification();
    }

    public GroupAllowanceSpecification withCodeOrName(final String search) {
        if (!ObjectUtils.isEmpty(search)) {
            specifications.add(
                    (root, q, cb) -> {
                        String likePattern = TextUtils.like(search);
                        // Tìm kiếm keyword nằm trong code hoặc name
                        // Sử dụng unaccent của postgresql để loại bỏ tiếng việt
                        return cb.or(
                                cb.like(
                                        cb.lower(cb.function("unaccent", String.class, root.get(GroupAllowanceEntity_.NAME))),
                                        likePattern
                                ),
                                cb.like(
                                        cb.lower(cb.function("unaccent", String.class, root.get(GroupAllowanceEntity_.CODE))),
                                        likePattern
                                )
                        );
                    }
            );
        }
        return this;
    }


    public GroupAllowanceSpecification withActive(Boolean active) {
        if (active != null) {
            specifications.add(
                    (root, q, cb) ->
                            cb.equal(root.get(GroupAllowanceEntity_.IS_ACTIVE), active)
            );
        }
        return this;
    }

    public GroupAllowanceSpecification withIds(List<Long> ids) {
        if (!ObjectUtils.isEmpty(ids)) {
            specifications.add(
                    (root, query, criteriaBuilder) ->
                            root.get(AbstractAuditingEntity_.id).in(ids)
            );
        }
        return this;
    }

    public Specification<GroupAllowanceEntity> build() {
        return (root, q, cb) -> cb.and(specifications.stream()
                .filter(Objects::nonNull)
                .map(s -> s.toPredicate(root, q, cb)).toArray(Predicate[]::new));
    }

}
