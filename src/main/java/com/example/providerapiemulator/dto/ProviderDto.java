package com.example.providerapiemulator.dto;

import java.util.UUID;

public record ProviderDto(
        UUID id,
        String fullName,
        String shortName,
        String commissionLevel,
        CommissionDetailsDto commission
) {}
