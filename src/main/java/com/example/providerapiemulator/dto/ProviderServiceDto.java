package com.example.providerapiemulator.dto;

import java.util.UUID;

public record ProviderServiceDto(
        UUID id,
        String name,
        UUID categoryId,
        UUID provId,
        UUID accountId,
        UUID commId
) {}
