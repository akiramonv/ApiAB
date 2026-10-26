package com.example.providerapiemulator.dto;

/** Счёт провайдера, таблица account_type. */
public record AccountDto(
        String id,
        String name,
        String paymentCode,
        String serviceIdPl,
        Boolean isManualSum,
        String providerId,
        String providerName,
        String commissionId,
        CommissionDetailsDto commission,
        Boolean enableMultiSelectServices,
        String mccCode,
        String qrInteractionModel
) {}
