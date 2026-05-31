package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.CategoryServiceDto;
import com.example.providerapiemulator.dto.CategoryServiceRequest;
import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final ProviderCatalogService service;

    public CategoryController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<CategoryServiceDto>> all() {
        return GenericResponse.ok(service.getAllCategories());
    }

    @GetMapping("/{id}")
    public GenericResponse<CategoryServiceDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getCategory(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<CategoryServiceDto>> byProvider(@PathVariable UUID providerId) {
        return GenericResponse.ok(service.getCategoriesByProvider(providerId));
    }

    @GetMapping("/by-parent/{parentId}")
    public GenericResponse<List<CategoryServiceDto>> byParent(@PathVariable UUID parentId) {
        return GenericResponse.ok(service.getCategoriesByParent(parentId));
    }

    @PostMapping
    public GenericResponse<CategoryServiceDto> create(@Valid @RequestBody CategoryServiceRequest dto) {
        return GenericResponse.ok(service.createCategory(dto), "Category created");
    }

    @PutMapping("/{id}")
    public GenericResponse<CategoryServiceDto> update(@PathVariable UUID id, @Valid @RequestBody CategoryServiceRequest dto) {
        return GenericResponse.ok(service.updateCategory(id, dto), "Category updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteCategory(id);
        return GenericResponse.ok(null, "Category deleted");
    }
}
