package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.PaymentDto;
import com.example.providerapiemulator.dto.PaymentRequest;
import com.example.providerapiemulator.dto.PaymentStatusRequest;
import com.example.providerapiemulator.model.PaymentStatus;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final ProviderCatalogService service;

    public PaymentController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<PaymentDto>> all() {
        return GenericResponse.ok(service.getAllPayments());
    }

    @GetMapping("/{id}")
    public GenericResponse<PaymentDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getPayment(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<PaymentDto>> byProvider(@PathVariable UUID providerId) {
        return GenericResponse.ok(service.getPaymentsByProvider(providerId));
    }

    @GetMapping("/by-status/{status}")
    public GenericResponse<List<PaymentDto>> byStatus(@PathVariable PaymentStatus status) {
        return GenericResponse.ok(service.getPaymentsByStatus(status));
    }

    @PostMapping
    public GenericResponse<PaymentDto> create(@Valid @RequestBody PaymentRequest dto) {
        return GenericResponse.ok(service.createPayment(dto), "Payment created");
    }

    @PatchMapping("/{id}/status")
    public GenericResponse<PaymentDto> updateStatus(@PathVariable UUID id, @Valid @RequestBody PaymentStatusRequest dto) {
        return GenericResponse.ok(service.updatePaymentStatus(id, dto), "Payment status updated");
    }

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qr(@PathVariable UUID id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(Base64.getDecoder().decode(service.getPaymentQrCode(id)));
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deletePayment(id);
        return GenericResponse.ok(null, "Payment deleted");
    }
}
