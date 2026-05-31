package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.RoleName;
import jakarta.validation.constraints.NotNull;

public record RoleRequest(
        @NotNull RoleName name
) {}
