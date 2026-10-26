package com.example.providerapiemulator.dto;

public record CategoryServiceDto(
        String id,
        String name,
        String parentCategoryId,
        String parentCategoryName,
        String icon,
        Boolean deleted,
        String providerId,
        String providerName
) {}
