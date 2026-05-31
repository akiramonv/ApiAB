package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record PaymentStatusRequest(
        @NotNull PaymentStatus status
) {}
