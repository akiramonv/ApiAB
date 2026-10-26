package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CommissionRequest(
        @NotNull CommissionType commissionType,
        @NotNull BigDecimal commissionValue
) {}
