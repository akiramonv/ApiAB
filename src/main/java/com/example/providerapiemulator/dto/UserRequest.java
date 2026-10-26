package com.example.providerapiemulator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UserRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String password,
        String providerId,
        Boolean deleted,
        Boolean mustChangePassword,
        List<String> roleIds,
        List<String> specializationIds
) {}
