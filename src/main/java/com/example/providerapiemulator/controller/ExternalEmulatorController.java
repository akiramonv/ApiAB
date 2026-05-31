package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.ServiceSearchRequest;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/emulator")
public class ExternalEmulatorController {

    private final ProviderCatalogService service;

    public ExternalEmulatorController(ProviderCatalogService service) {
        this.service = service;
    }

    @PostMapping("/execute")
    public GenericResponse<Map<String, Object>> execute(@Valid @RequestBody ServiceSearchRequest request) {
        return GenericResponse.ok(service.executeExternalRequest(request.operation(), request.params()));
    }
}
