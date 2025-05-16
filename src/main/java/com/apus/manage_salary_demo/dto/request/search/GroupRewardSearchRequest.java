package com.apus.manage_salary_demo.dto.request.search;

import com.apus.manage_salary_demo.dto.request.FilterRequest;
import com.apus.manage_salary_demo.entity.GroupRewardEntity;
import com.apus.manage_salary_demo.repository.specifiation.GroupRewardSpecification;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupRewardSearchRequest extends FilterRequest<GroupRewardEntity> {
    List<Long> ids;
    String search;

    @Override
    public Specification<GroupRewardEntity> specification() {
        return GroupRewardSpecification.builder()
                .withIds(ids)
                .withCodeOrName(search)
                .build();
    }
}
