package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.UserDto;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ProviderCatalogService service;

    public UserController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<UserDto>> all() {
        return GenericResponse.ok(service.getAllUsers());
    }

    @GetMapping("/{id}")
    public GenericResponse<UserDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getUser(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<UserDto>> byProvider(@PathVariable UUID providerId) {
        return GenericResponse.ok(service.getUsersByProvider(providerId));
    }

    @GetMapping("/by-specialization/{specialId}")
    public GenericResponse<List<UserDto>> bySpecialization(@PathVariable UUID specialId) {
        return GenericResponse.ok(service.getUsersBySpecialization(specialId));
    }

    @PostMapping
    public GenericResponse<UserDto> create(@Valid @RequestBody UserDto dto) {
        return GenericResponse.ok(service.createUser(dto), "User created");
    }

    @PutMapping("/{id}")
    public GenericResponse<UserDto> update(@PathVariable UUID id, @Valid @RequestBody UserDto dto) {
        return GenericResponse.ok(service.updateUser(id, dto), "User updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteUser(id);
        return GenericResponse.ok(null, "User deleted");
    }
}
