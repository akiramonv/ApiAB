package com.example.providerapiemulator.dto;

import java.util.UUID;

public record CategoryServiceDto(
        UUID id,
        String name,
        UUID prntCategory,
        UUID provId
) {}
