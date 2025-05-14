package com.apus.manage_salary_demo.mapper.util;

import java.util.List;
import java.util.stream.Stream;

public interface IncludeTypeMapperUtil {

    default List<String> mapIncludeTypeToList(String includeType) {
        if (includeType == null || includeType.isBlank()) return List.of();
        return Stream.of(includeType.split(","))
                .map(String::trim)
                .map(String::valueOf)
                .toList();
    }

    default String mapIncludeTypesToString(List<?> includeTypes) {
        if (includeTypes == null || includeTypes.isEmpty()) return null;
        return String.join(",", includeTypes.stream().map(Object::toString).toList());
    }
}
