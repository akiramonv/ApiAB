package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.CommissionLevelDto;
import com.example.providerapiemulator.dto.CommissionLevelRequest;
import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commission-levels")
public class CommissionLevelController {

    private final ProviderCatalogService service;

    public CommissionLevelController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<CommissionLevelDto>> all() {
        return GenericResponse.ok(service.getAllCommissionLevels());
    }

    @GetMapping("/{id}")
    public GenericResponse<CommissionLevelDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getCommissionLevel(id));
    }

    @PostMapping
    public GenericResponse<CommissionLevelDto> create(@Valid @RequestBody CommissionLevelRequest dto) {
        return GenericResponse.ok(service.createCommissionLevel(dto), "Commission level created");
    }

    @PutMapping("/{id}")
    public GenericResponse<CommissionLevelDto> update(@PathVariable UUID id, @Valid @RequestBody CommissionLevelRequest dto) {
        return GenericResponse.ok(service.updateCommissionLevel(id, dto), "Commission level updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteCommissionLevel(id);
        return GenericResponse.ok(null, "Commission level deleted");
    }
}
