package com.example.providerapiemulator.dto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String name,
        String email,
        String password,
        UUID provId
) {}
