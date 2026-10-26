package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryServiceRequest(
        @NotBlank String name,
        String parentCategoryId,
        String icon,
        Boolean deleted,
        @NotBlank String providerId
) {}
