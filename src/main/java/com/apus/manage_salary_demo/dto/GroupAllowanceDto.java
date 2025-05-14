package com.apus.manage_salary_demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupAllowanceDto {
    private Long id;

    @NotBlank(message = "Code must not be blank")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    private ParentDto parent;

    private String description;

    @NotNull(message = "isActive must not be null")
    private Boolean isActive;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParentDto {
        private Long id;
        private String code;
        private String name;
    }
}
