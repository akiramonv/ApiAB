package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.UserDto;
import com.example.providerapiemulator.dto.UserRequest;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ProviderCatalogService service;

    public UserController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<UserDto>> all(@RequestParam(defaultValue = "false") boolean includeDeleted) {
        return GenericResponse.ok(service.getAllUsers(includeDeleted));
    }

    @GetMapping("/{id}")
    public GenericResponse<UserDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getUser(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<UserDto>> byProvider(@PathVariable String providerId) {
        return GenericResponse.ok(service.getUsersByProvider(providerId));
    }

    @GetMapping("/by-specialization/{specialId}")
    public GenericResponse<List<UserDto>> bySpecialization(@PathVariable String specialId) {
        return GenericResponse.ok(service.getUsersBySpecialization(specialId));
    }

    @PostMapping
    public GenericResponse<UserDto> create(@Valid @RequestBody UserRequest dto) {
        return GenericResponse.ok(service.createUser(dto), "User created");
    }

    @PutMapping("/{id}")
    public GenericResponse<UserDto> update(@PathVariable String id, @Valid @RequestBody UserRequest dto) {
        return GenericResponse.ok(service.updateUser(id, dto), "User updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteUser(id);
        return GenericResponse.ok(null, "User marked as deleted");
    }
}
