package com.example.providerapiemulator.dto;

import java.util.UUID;

public record ProviderDto(
        UUID id,
        String fullName,
        String shortName,
        UUID commLvl,
        UUID commId
) {}
