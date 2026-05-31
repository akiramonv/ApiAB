package com.example.providerapiemulator.dto;

import java.util.UUID;
import java.util.List;

public record ProviderServiceDto(
        UUID id,
        String name,
        String categoryName,
        String providerName,
        String accountName,
        CommissionDetailsDto commission,
        List<String> specializations
) {}
