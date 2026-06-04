package com.example.providerapiemulator.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProviderServiceDto(
        UUID id,
        String name,
        BigDecimal price,
        String categoryName,
        String providerName,
        String accountName,
        CommissionDetailsDto commission,
        List<String> specializations
) {}
