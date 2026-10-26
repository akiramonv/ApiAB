package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.PaymentDto;
import com.example.providerapiemulator.dto.PaymentRequest;
import com.example.providerapiemulator.dto.PaymentStatusRequest;
import com.example.providerapiemulator.dto.PaymentTransactDto;
import com.example.providerapiemulator.dto.PaymentTransactRequest;
import com.example.providerapiemulator.model.PaymentStatus;
import com.example.providerapiemulator.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<PaymentDto>> all(@RequestParam(defaultValue = "false") boolean includeDeleted) {
        return GenericResponse.ok(service.getAllPayments(includeDeleted));
    }

    @GetMapping("/{id}")
    public GenericResponse<PaymentDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getPayment(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<PaymentDto>> byProvider(@PathVariable String providerId) {
        return GenericResponse.ok(service.getPaymentsByProvider(providerId));
    }

    @GetMapping("/by-user/{userId}")
    public GenericResponse<List<PaymentDto>> byUser(@PathVariable String userId) {
        return GenericResponse.ok(service.getPaymentsByUser(userId));
    }

    @GetMapping("/by-status/{status}")
    public GenericResponse<List<PaymentDto>> byStatus(@PathVariable PaymentStatus status) {
        return GenericResponse.ok(service.getPaymentsByStatus(status));
    }

    @GetMapping("/by-paycode/{paycode}")
    public GenericResponse<List<PaymentDto>> byPaycode(@PathVariable String paycode) {
        return GenericResponse.ok(service.getPaymentsByPaycode(paycode));
    }

    @PostMapping
    public GenericResponse<PaymentDto> create(@Valid @RequestBody PaymentRequest dto) {
        return GenericResponse.ok(service.createPayment(dto), "Payment created");
    }

    @PatchMapping("/{id}/status")
    public GenericResponse<PaymentDto> updateStatus(@PathVariable String id,
                                                    @Valid @RequestBody PaymentStatusRequest dto) {
        return GenericResponse.ok(service.updatePaymentStatus(id, dto), "Payment status updated");
    }

    @GetMapping("/{id}/transacts")
    public GenericResponse<List<PaymentTransactDto>> transacts(@PathVariable String id) {
        return GenericResponse.ok(service.getTransacts(id));
    }

    @PostMapping("/{id}/transacts")
    public GenericResponse<PaymentTransactDto> addTransact(@PathVariable String id,
                                                           @Valid @RequestBody PaymentTransactRequest dto) {
        return GenericResponse.ok(service.addTransact(id, dto), "Transact created");
    }

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qr(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(Base64.getDecoder().decode(service.getPaymentQrCode(id)));
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deletePayment(id);
        return GenericResponse.ok(null, "Payment marked as deleted");
    }
}
