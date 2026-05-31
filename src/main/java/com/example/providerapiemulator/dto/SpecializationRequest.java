package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SpecializationRequest(
        @NotBlank String name,
        @NotNull UUID provId
) {}
