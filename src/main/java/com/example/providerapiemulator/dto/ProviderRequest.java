package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionLevelName;
import jakarta.validation.constraints.NotBlank;

public record ProviderRequest(
        @NotBlank String name,
        String account,
        Integer code,
        String inn,
        String password,
        CommissionLevelName commissionLevel,
        String commissionId,
        String organisationId,
        String address,
        String phoneNumber,
        String postalCode,
        String director,
        String chiefAccountant,
        String bik,
        String gni,
        String okpo,
        String bankAddress,
        String bankFilialId,
        String levelIdPl,
        Boolean paycodeManual,
        Boolean fioInnHide,
        Boolean disableStatQr,
        Boolean requireInnForPayment
) {}
