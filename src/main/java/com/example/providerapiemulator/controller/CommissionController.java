package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.CommissionDto;
import com.example.providerapiemulator.dto.CommissionRequest;
import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public GenericResponse<CommissionDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getCommission(id));
    }

    @PostMapping
    public GenericResponse<CommissionDto> create(@Valid @RequestBody CommissionRequest dto) {
        return GenericResponse.ok(service.createCommission(dto), "Commission created");
    }

    @PutMapping("/{id}")
    public GenericResponse<CommissionDto> update(@PathVariable String id, @Valid @RequestBody CommissionRequest dto) {
        return GenericResponse.ok(service.updateCommission(id, dto), "Commission updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteCommission(id);
        return GenericResponse.ok(null, "Commission deleted");
    }
}
