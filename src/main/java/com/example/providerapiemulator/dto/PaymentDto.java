package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentDto(
        UUID id,
        BigDecimal sum,
        BigDecimal fee,
        PaymentStatus status,
        String providerName,
        String qrLink,
        String qrCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
