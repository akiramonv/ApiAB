package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountRequest(
        @NotBlank String name,
        String paymentCode,
        String serviceIdPl,
        Boolean isManualSum,
        @NotBlank String providerId,
        String commissionId,
        Boolean enableMultiSelectServices,
        String mccCode,
        String qrInteractionModel
) {}
