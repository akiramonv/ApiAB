package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.ProviderDto;
import com.example.providerapiemulator.dto.ProviderRequest;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public GenericResponse<ProviderDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getProvider(id));
    }

    @GetMapping("/by-organisation/{organisationId}")
    public GenericResponse<List<ProviderDto>> byOrganisation(@PathVariable String organisationId) {
        return GenericResponse.ok(service.getProvidersByOrganisation(organisationId));
    }

    @PostMapping
    public GenericResponse<ProviderDto> create(@Valid @RequestBody ProviderRequest dto) {
        return GenericResponse.ok(service.createProvider(dto), "Provider created");
    }

    @PutMapping("/{id}")
    public GenericResponse<ProviderDto> update(@PathVariable String id, @Valid @RequestBody ProviderRequest dto) {
        return GenericResponse.ok(service.updateProvider(id, dto), "Provider updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteProvider(id);
        return GenericResponse.ok(null, "Provider deleted");
    }
}
