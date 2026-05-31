package com.example.providerapiemulator.dto;

import java.util.UUID;

public record SpecializationDto(
        UUID id,
        String name,
        String providerName
) {}
