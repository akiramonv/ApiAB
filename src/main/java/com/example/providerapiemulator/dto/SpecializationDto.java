package com.example.providerapiemulator.dto;

public record SpecializationDto(
        String id,
        String name,
        Boolean deleted,
        String providerId,
        String providerName
) {}
