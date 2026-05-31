package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionLevelName;

import java.util.UUID;

public record CommissionLevelDto(
        UUID id,
        CommissionLevelName name
) {}
