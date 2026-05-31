package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionType;

import java.math.BigDecimal;
import java.util.UUID;

public record CommissionDto(
        UUID id,
        CommissionType commissionType,
        BigDecimal commissionValue
) {}
