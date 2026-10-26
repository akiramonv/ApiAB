package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.AccountDto;
import com.example.providerapiemulator.dto.AccountRequest;
import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.service.ProviderCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Счета провайдеров - таблица account_type. */
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
    public GenericResponse<AccountDto> one(@PathVariable String id) {
        return GenericResponse.ok(service.getAccount(id));
    }

    @GetMapping("/by-provider/{providerId}")
    public GenericResponse<List<AccountDto>> byProvider(@PathVariable String providerId) {
        return GenericResponse.ok(service.getAccountsByProvider(providerId));
    }

    @PostMapping
    public GenericResponse<AccountDto> create(@Valid @RequestBody AccountRequest dto) {
        return GenericResponse.ok(service.createAccount(dto), "Account created");
    }

    @PutMapping("/{id}")
    public GenericResponse<AccountDto> update(@PathVariable String id, @Valid @RequestBody AccountRequest dto) {
        return GenericResponse.ok(service.updateAccount(id, dto), "Account updated");
    }

    @DeleteMapping("/{id}")
    public GenericResponse<Void> delete(@PathVariable String id) {
        service.deleteAccount(id);
        return GenericResponse.ok(null, "Account deleted");
    }
}
