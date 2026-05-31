package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.AccountDto;
import com.example.providerapiemulator.dto.AccountRequest;
import com.example.providerapiemulator.dto.GenericResponse;
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
@RequestMapping("/api/accounts")
public class AccountController {

    private final ProviderCatalogService service;

    public AccountController(ProviderCatalogService service) {
        this.service = service;
    }

    @GetMapping
    public GenericResponse<List<AccountDto>> all() {
        return GenericResponse.ok(service.getAllAccounts());
    }

    @GetMapping("/{id}")
    public GenericResponse<AccountDto> one(@PathVariable UUID id) {
        return GenericResponse.ok(service.getAccount(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<AccountDto>> byProvider(@PathVariable UUID providerId) {
        return GenericResponse.ok(service.getAccountsByProvider(providerId));
    }

    @PostMapping
    public GenericResponse<AccountDto> create(@Valid @RequestBody AccountRequest dto) {
        return GenericResponse.ok(service.createAccount(dto), "Account created");
    }

    @PutMapping("/{id}")
    public GenericResponse<AccountDto> update(@PathVariable UUID id, @Valid @RequestBody AccountRequest dto) {
        return GenericResponse.ok(service.updateAccount(id, dto), "Account updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable UUID id) {
        service.deleteAccount(id);
        return GenericResponse.ok(null, "Account deleted");
    }
}
