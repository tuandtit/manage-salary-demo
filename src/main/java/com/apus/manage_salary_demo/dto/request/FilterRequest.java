package com.apus.manage_salary_demo.dto.request;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class FilterRequest<T> {
    private int page = 0;
    private int size = 20;
    private Map<String, String> orders = new HashMap<>();

    public Pageable pageable() {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        if (CollectionUtils.isEmpty(orders)) {
            orders.put("createdAt", "DESC");
        }
        Sort sortable = sortable(orders);
        return PageRequest.of(page, size, sortable);
    }

    public Sort sortable(Map<String, String> orders) {
        List<Sort.Order> sortableList = new ArrayList<>();
        orders.forEach((key, value) -> {
            Sort.Direction direction = Sort.Direction.DESC.name().equals(value) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order order = new Sort.Order(direction, key);
            sortableList.add(order);
        });
        return Sort.by(sortableList);

    }

    public abstract Specification<T> specification();
}
