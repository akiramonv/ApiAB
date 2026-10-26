package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.ProviderServiceDto;
import com.example.providerapiemulator.dto.ProviderServiceRequest;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ProviderServiceController {

    private final ProviderCatalogService service;

    public ProviderServiceController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<ProviderServiceDto>> all(
            @RequestParam(defaultValue = "false") boolean includeDeleted) {
        return GenericResponse.ok(service.getAllServices(includeDeleted));
    }

    @GetMapping("/{id}")
    public GenericResponse<ProviderServiceDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getService(id));
    }

    @GetMapping("/by-category/{categoryId}")
    public GenericResponse<List<ProviderServiceDto>> byCategory(@PathVariable String categoryId) {
        return GenericResponse.ok(service.getServicesByCategory(categoryId));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<ProviderServiceDto>> byProvider(@PathVariable String providerId) {
        return GenericResponse.ok(service.getServicesByProvider(providerId));
    }

    @GetMapping("/by-account/{accountId}")
    public GenericResponse<List<ProviderServiceDto>> byAccount(@PathVariable String accountId) {
        return GenericResponse.ok(service.getServicesByAccount(accountId));
    }

    @GetMapping("/by-user/{userId}")
    public GenericResponse<List<ProviderServiceDto>> byUser(@PathVariable String userId) {
        return GenericResponse.ok(service.getServicesByUserSpecialization(userId));
    }

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qr(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(Base64.getDecoder().decode(service.getServiceQrCode(id)));
    }

    @PostMapping
    public GenericResponse<ProviderServiceDto> create(@Valid @RequestBody ProviderServiceRequest dto) {
        return GenericResponse.ok(service.createService(dto), "Service created");
    }

    @PutMapping("/{id}")
    public GenericResponse<ProviderServiceDto> update(@PathVariable String id,
                                                      @Valid @RequestBody ProviderServiceRequest dto) {
        return GenericResponse.ok(service.updateService(id, dto), "Service updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteService(id);
        return GenericResponse.ok(null, "Service marked as deleted");
    }
}
