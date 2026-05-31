package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ProviderServiceRequest(
        @NotBlank String name,
        @NotNull UUID categoryId,
        @NotNull UUID provId,
        @NotNull UUID accountId,
        UUID commId,
        List<UUID> specializationIds
) {}
