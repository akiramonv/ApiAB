package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

public record ProviderServiceRequest(
        @NotBlank String name,
        @DecimalMin("0.00") BigDecimal price,
        Integer code,
        String checkSource,
        String categoryId,
        @NotBlank String providerId,
        String accountTypeId,
        String commissionId,
        Boolean increase,
        String increaseText,
        String serviceType,
        String serviceAccount,
        Boolean deleted,
        String serviceIdPl,
        String qrLink,
        List<String> specializationIds
) {}
