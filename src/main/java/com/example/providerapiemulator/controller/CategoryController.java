package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.CategoryServiceDto;
import com.example.providerapiemulator.dto.CategoryServiceRequest;
import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final ProviderCatalogService service;

    public CategoryController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<CategoryServiceDto>> all(
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
        return GenericResponse.ok(service.getAllCategories(includeDeleted));
    }

    @GetMapping("/{id}")
    public GenericResponse<CategoryServiceDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getCategory(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<CategoryServiceDto>> byProvider(@PathVariable String providerId) {
        return GenericResponse.ok(service.getCategoriesByProvider(providerId));
    }

    @GetMapping("/by-parent/{parentId}")
    public GenericResponse<List<CategoryServiceDto>> byParent(@PathVariable String parentId) {
        return GenericResponse.ok(service.getCategoriesByParent(parentId));
    }

    @PostMapping
    public GenericResponse<CategoryServiceDto> create(@Valid @RequestBody CategoryServiceRequest dto) {
        return GenericResponse.ok(service.createCategory(dto), "Category created");
    }

    @PutMapping("/{id}")
    public GenericResponse<CategoryServiceDto> update(@PathVariable String id,
                                                      @Valid @RequestBody CategoryServiceRequest dto) {
        return GenericResponse.ok(service.updateCategory(id, dto), "Category updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteCategory(id);
        return GenericResponse.ok(null, "Category marked as deleted");
    }
}
