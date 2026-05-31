package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record ServiceSearchRequest(
        @NotBlank String operation,
        Map<String, Object> params
) {}
