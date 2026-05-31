package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CommissionRequest(
        @NotNull CommissionType commissionType,
        @NotNull @DecimalMin("0.00") BigDecimal commissionValue
) {}
