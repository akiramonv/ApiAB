package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.SpecializationDto;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final ProviderCatalogService service;

    public SpecializationController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<SpecializationDto>> all() {
        return GenericResponse.ok(service.getAllSpecializations());
    }

    @GetMapping("/{id}")
    public GenericResponse<SpecializationDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getSpecialization(id));
    }

        @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<SpecializationDto>> byProvider(@PathVariable UUID providerId) {
        return GenericResponse.ok(service.getSpecializationsByProvider(providerId));
    }

    @PostMapping
    public GenericResponse<SpecializationDto> create(@Valid @RequestBody SpecializationDto dto) {
        return GenericResponse.ok(service.createSpecialization(dto), "Specialization created");
    }

    @PutMapping("/{id}")
    public GenericResponse<SpecializationDto> update(@PathVariable UUID id, @Valid @RequestBody SpecializationDto dto) {
        return GenericResponse.ok(service.updateSpecialization(id, dto), "Specialization updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteSpecialization(id);
        return GenericResponse.ok(null, "Specialization deleted");
    }
}
