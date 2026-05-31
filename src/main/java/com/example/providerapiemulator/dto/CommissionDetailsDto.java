package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionType;

import java.math.BigDecimal;

public record CommissionDetailsDto(
        CommissionType commissionType,
        BigDecimal commissionValue
) {}
