package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.CommissionLevelDto;
import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.service.ProviderCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * В billing нет таблицы уровней комиссии - это фиксированный набор значений
 * providers.commissionlevel, поэтому endpoint только на чтение.
 */
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

    @GetMapping("/{name}")
    public GenericResponse<CommissionLevelDto> one(@PathVariable String name) {
        return GenericResponse.ok(service.getCommissionLevel(name));
    }
}
