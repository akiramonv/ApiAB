package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.RoleDto;
import com.example.providerapiemulator.dto.RoleRequest;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public GenericResponse<RoleDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getRole(id));
    }

    @GetMapping("/by-user/{userId}")
    public GenericResponse<List<RoleDto>> byUser(@PathVariable String userId) {
        return GenericResponse.ok(service.getRolesByUser(userId));
    }

    @PostMapping
    public GenericResponse<RoleDto> create(@Valid @RequestBody RoleRequest dto) {
        return GenericResponse.ok(service.createRole(dto), "Role created");
    }

    @PutMapping("/{id}")
    public GenericResponse<RoleDto> update(@PathVariable String id, @Valid @RequestBody RoleRequest dto) {
        return GenericResponse.ok(service.updateRole(id, dto), "Role updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteRole(id);
        return GenericResponse.ok(null, "Role deleted");
    }
}
