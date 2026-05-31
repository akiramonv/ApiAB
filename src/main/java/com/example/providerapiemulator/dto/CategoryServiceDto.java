package com.example.providerapiemulator.dto;

import java.util.UUID;

public record CategoryServiceDto(
        UUID id,
        String name,
        String parentCategoryName,
        String providerName
) {}
