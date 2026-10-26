package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.SpecializationDto;
import com.example.providerapiemulator.dto.SpecializationRequest;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final ProviderCatalogService service;

    public SpecializationController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<SpecializationDto>> all(
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
        return GenericResponse.ok(service.getAllSpecializations(includeDeleted));
    }

    @GetMapping("/{id}")
    public GenericResponse<SpecializationDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getSpecialization(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<SpecializationDto>> byProvider(@PathVariable String providerId) {
        return GenericResponse.ok(service.getSpecializationsByProvider(providerId));
    }

    @PostMapping
    public GenericResponse<SpecializationDto> create(@Valid @RequestBody SpecializationRequest dto) {
        return GenericResponse.ok(service.createSpecialization(dto), "Specialization created");
    }

    @PutMapping("/{id}")
    public GenericResponse<SpecializationDto> update(@PathVariable String id,
                                                     @Valid @RequestBody SpecializationRequest dto) {
        return GenericResponse.ok(service.updateSpecialization(id, dto), "Specialization updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteSpecialization(id);
        return GenericResponse.ok(null, "Specialization marked as deleted");
    }
}
