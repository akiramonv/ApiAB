package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.PaymentGetTime;
import com.example.providerapiemulator.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PaymentDto(
        String id,
        /** null, если платёж на произвольную сумму (в БД sum = "any"). */
        BigDecimal sum,
        boolean anySum,
        BigDecimal commission,
        BigDecimal totalSum,
        BigDecimal paidSum,
        BigDecimal remainingSum,
        Integer balance,
        PaymentStatus status,
        PaymentGetTime paymentGetTime,
        String paycode,
        String clientType,
        String commissionLevel,
        String sourceCheck,
        Boolean invoice,
        Boolean deleted,
        String inn,
        String fio,
        List<CheckFieldDto> checkFields,
        LocalDateTime deadlineDate,
        LocalDateTime createdDate,
        String providerUserId,
        String providerUserName,
        String providerId,
        String providerName,
        String qrLink,
        List<PaymentServiceDto> services,
        List<PaymentTransactDto> transacts
) {}
