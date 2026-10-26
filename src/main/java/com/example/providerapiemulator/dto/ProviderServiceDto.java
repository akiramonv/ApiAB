package com.example.providerapiemulator.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProviderServiceDto(
        String id,
        String name,
        Integer code,
        BigDecimal price,
        BigDecimal servicePrice,
        BigDecimal totalSum,
        String checkSource,
        String categoryId,
        String categoryName,
        String providerId,
        String providerName,
        String accountTypeId,
        String accountName,
        String commissionId,
        CommissionDetailsDto commission,
        Boolean increase,
        String increaseText,
        String serviceType,
        String serviceAccount,
        Boolean deleted,
        String serviceIdPl,
        String qrLink,
        Boolean hasQrCode,
        List<String> specializations
) {}
