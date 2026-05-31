package com.example.providerapiemulator.dto;

import java.util.List;
import java.util.UUID;

public record UserDto(
        UUID id,
        String name,
        String email,
        String password,
        String providerName,
        List<String> roles,
        List<String> specializations
) {}
