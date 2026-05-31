package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        @NotNull @DecimalMin("0.00") BigDecimal sum,
        @DecimalMin("0.00") BigDecimal fee,
        PaymentStatus status,
        @NotNull UUID provId
) {}
