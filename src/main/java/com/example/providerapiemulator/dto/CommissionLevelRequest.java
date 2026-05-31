package com.example.providerapiemulator.dto;

import com.example.providerapiemulator.model.CommissionLevelName;
import jakarta.validation.constraints.NotNull;

public record CommissionLevelRequest(
        @NotNull CommissionLevelName name
) {}
