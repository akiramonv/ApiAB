package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.RoleName;

import java.util.UUID;

public record RoleDto(
        UUID id,
        RoleName name
) {}
