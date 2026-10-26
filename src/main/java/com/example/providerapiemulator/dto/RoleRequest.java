package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
        @NotBlank String name
) {}
