package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.PaymentGetTime;
import com.example.providerapiemulator.model.PaymentStatus;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Суммы указываются в сомах, в БД они уходят в тыйынах.
 * Если переданы serviceIds - сумма берётся из фиксированных сумм этих услуг,
 * либо из sum, когда у услуг сумма не задана.
 */
public record PaymentRequest(
        @DecimalMin("0.00") BigDecimal sum,
        /** true - платёж на произвольную сумму (payment.sum = "any"). */
        Boolean anySum,
        @DecimalMin("0.00") BigDecimal commission,
        PaymentStatus status,
        PaymentGetTime paymentGetTime,
        String paycode,
        String clientType,
        String inn,
        String fio,
        List<CheckFieldDto> checkFields,
        String providerUserId,
        String providerId,
        List<String> serviceIds,
        LocalDateTime deadlineDate,
        Boolean invoice
) {}
