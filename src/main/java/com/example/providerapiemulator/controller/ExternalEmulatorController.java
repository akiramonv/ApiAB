package com.example.providerapiemulator.controller;

import com.example.providerapiemulator.dto.GenericResponse;
import com.example.providerapiemulator.dto.ServiceSearchRequest;
import com.example.providerapiemulator.service.EmulatorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/emulator")
public class ExternalEmulatorController {

    private final EmulatorService service;

    public ExternalEmulatorController(EmulatorService service) {
        this.service = service;
    }

    @PostMapping("/execute")
    public GenericResponse<Map<String, Object>> execute(@Valid @RequestBody ServiceSearchRequest request) {
        return GenericResponse.ok(service.execute(request.operation(), request.params()));
    }
}
