package com.example.providerapiemulator.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record UserDto(
        String id,
        String name,
        String email,
        String password,
        String providerId,
        String providerName,
        Boolean deleted,
        Integer failedLoginAttempts,
        OffsetDateTime lockoutEnd,
        Boolean mustChangePassword,
        List<String> roles,
        List<String> specializations
) {}
