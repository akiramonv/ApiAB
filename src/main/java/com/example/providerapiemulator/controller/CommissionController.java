package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.CommissionDto;
import com.example.providerapiemulator.dto.CommissionRequest;
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
@RequestMapping("/api/commissions")
public class CommissionController {

    private final ProviderCatalogService service;

    public CommissionController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<CommissionDto>> all() {
        return GenericResponse.ok(service.getAllCommissions());
    }

    @GetMapping("/{id}")
    public GenericResponse<CommissionDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getCommission(id));
    }

    @PostMapping
    public GenericResponse<CommissionDto> create(@Valid @RequestBody CommissionRequest dto) {
        return GenericResponse.ok(service.createCommission(dto), "Commission created");
    }

    @PutMapping("/{id}")
    public GenericResponse<CommissionDto> update(@PathVariable UUID id, @Valid @RequestBody CommissionRequest dto) {
        return GenericResponse.ok(service.updateCommission(id, dto), "Commission updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteCommission(id);
        return GenericResponse.ok(null, "Commission deleted");
    }
}
