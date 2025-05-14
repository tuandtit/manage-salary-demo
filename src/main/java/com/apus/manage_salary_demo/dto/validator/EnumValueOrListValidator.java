package com.apus.manage_salary_demo.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValueOrListValidator implements ConstraintValidator<EnumValueOrList, Object> {

    private Set<String> acceptedValues;
    private String name;

    @Override
    public void initialize(EnumValueOrList annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
        name = annotation.name();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        boolean valid = false;

        if (value instanceof String str) {
            valid = acceptedValues.contains(str.toUpperCase());
        } else if (value instanceof List) {
            valid = ((List<?>) value).stream()
                    .allMatch(item -> item instanceof String str && acceptedValues.contains(str.toUpperCase()));
        }

        if (!valid) {
            // Build custom error message
            String message = String.format("%s must be one of: %s", name, acceptedValues);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return valid;
    }
}

