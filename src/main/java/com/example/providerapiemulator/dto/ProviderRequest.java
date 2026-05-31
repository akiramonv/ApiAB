package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProviderRequest(
        @NotBlank String fullName,
        String shortName,
        @NotNull UUID commLvl,
        UUID commId
) {}
