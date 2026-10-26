package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;

public record SpecializationRequest(
        @NotBlank String name,
        @NotBlank String providerId,
        Boolean deleted
) {}
