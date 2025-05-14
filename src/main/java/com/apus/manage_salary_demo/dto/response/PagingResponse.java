package com.apus.manage_salary_demo.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingResponse<T> {
    List<T> content = new ArrayList<>();
    int page;
    int size;
    String sort;
    long totalElements;
    int totalPages;
    long numberOfElements;

    public static <T> PagingResponse<T> from(Page<T> page) {
        final var response = new PagingResponse<T>();
        response.setContent(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setSort(page.getSort().toString());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setNumberOfElements(page.getNumberOfElements());

        return response;
    }
}
