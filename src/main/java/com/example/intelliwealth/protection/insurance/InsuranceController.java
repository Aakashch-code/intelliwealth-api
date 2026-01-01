package com.example.intelliwealth.protection.insurance;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {

    private final InsuranceService service;

    public InsuranceController(InsuranceService service) {
        this.service = service;
    }

    @PostMapping
    public InsuranceResponseDTO create(@RequestBody InsuranceRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<InsuranceResponseDTO> getAll() {
        return service.getAll();
    }
}
