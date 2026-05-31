package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.ProviderServiceDto;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class ProviderServiceController {

    private final ProviderCatalogService service;

    public ProviderServiceController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<ProviderServiceDto>> all() {
        return GenericResponse.ok(service.getAllServices());
    }

    @GetMapping("/{id}")
    public GenericResponse<ProviderServiceDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getService(id));
    }

    @GetMapping("/by-category/{categoryId}")
    public GenericResponse<List<ProviderServiceDto>> byCategory(@PathVariable UUID categoryId) {
        return GenericResponse.ok(service.getServicesByCategory(categoryId));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<ProviderServiceDto>> byProvider(@PathVariable UUID providerId) {
        return GenericResponse.ok(service.getServicesByProvider(providerId));
    }

    @GetMapping("/by-user/{userId}")
    public GenericResponse<List<ProviderServiceDto>> byUser(@PathVariable UUID userId) {
        return GenericResponse.ok(service.getServicesByUserSpecialization(userId));
    }

    @PostMapping
    public GenericResponse<ProviderServiceDto> create(@Valid @RequestBody ProviderServiceDto dto) {
        return GenericResponse.ok(service.createService(dto), "Service created");
    }

    @PutMapping("/{id}")
    public GenericResponse<ProviderServiceDto> update(@PathVariable UUID id, @Valid @RequestBody ProviderServiceDto dto) {
        return GenericResponse.ok(service.updateService(id, dto), "Service updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteService(id);
        return GenericResponse.ok(null, "Service deleted");
    }
}
