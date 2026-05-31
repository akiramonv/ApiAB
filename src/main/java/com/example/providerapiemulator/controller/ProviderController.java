package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.ProviderDto;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/providers")
public class ProviderController {

    private final ProviderCatalogService service;

    public ProviderController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<ProviderDto>> all() {
        return GenericResponse.ok(service.getAllProviders());
    }

    @GetMapping("/{id}")
    public GenericResponse<ProviderDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getProvider(id));
    }

    @PostMapping
    public GenericResponse<ProviderDto> create(@Valid @RequestBody ProviderDto dto) {
        return GenericResponse.ok(service.createProvider(dto), "Provider created");
    }

    @PutMapping("/{id}")
    public GenericResponse<ProviderDto> update(@PathVariable UUID id, @Valid @RequestBody ProviderDto dto) {
        return GenericResponse.ok(service.updateProvider(id, dto), "Provider updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteProvider(id);
        return GenericResponse.ok(null, "Provider deleted");
    }
}
