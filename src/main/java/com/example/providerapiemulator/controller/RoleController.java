package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.RoleDto;
import com.example.providerapiemulator.dto.RoleRequest;
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
@RequestMapping("/api/roles")
public class RoleController {

    private final ProviderCatalogService service;

    public RoleController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<RoleDto>> all() {
        return GenericResponse.ok(service.getAllRoles());
    }

    @GetMapping("/{id}")
    public GenericResponse<RoleDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getRole(id));
    }

    @GetMapping("/by-user/{userId}")
    public GenericResponse<List<RoleDto>> byUser(@PathVariable UUID userId) {
        return GenericResponse.ok(service.getRolesByUser(userId));
    }

    @PostMapping
    public GenericResponse<RoleDto> create(@Valid @RequestBody RoleRequest dto) {
        return GenericResponse.ok(service.createRole(dto), "Role created");
    }

    @PutMapping("/{id}")
    public GenericResponse<RoleDto> update(@PathVariable UUID id, @Valid @RequestBody RoleRequest dto) {
        return GenericResponse.ok(service.updateRole(id, dto), "Role updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteRole(id);
        return GenericResponse.ok(null, "Role deleted");
    }
}
