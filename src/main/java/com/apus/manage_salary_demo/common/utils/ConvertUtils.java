package com.apus.manage_salary_demo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils {

    public static String joinLongSet(Set<Long> longs) {
        if (longs == null || longs.isEmpty()) {
            return "";
        }
        return longs.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
