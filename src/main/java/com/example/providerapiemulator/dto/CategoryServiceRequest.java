package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CategoryServiceRequest(
        @NotBlank String name,
        UUID prntCategory,
        @NotNull UUID provId
) {}
