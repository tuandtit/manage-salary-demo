package com.apus.manage_salary_demo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils implements Converter<String, List<String>> {

    public static String joinLongSet(Set<Long> longs) {
        if (longs == null || longs.isEmpty()) {
            return "";
        }
        return longs.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }


    @Override
    public List<String> convert(String source) {
        return Arrays.stream(source.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
