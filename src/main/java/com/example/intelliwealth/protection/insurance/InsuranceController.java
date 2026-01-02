package com.example.intelliwealth.protection.insurance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance")
@Tag(name = "Insurance Management", description = "APIs for managing user insurance policies (Life, Health, General)")
public class InsuranceController {

    private final InsuranceService service;

    public InsuranceController(InsuranceService service) {
        this.service = service;
    }

    // --- CREATE ---
    @Operation(summary = "Create a new policy", description = "Adds a new insurance policy to the portfolio with validation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Policy created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid attributes for category")
    })
    @PostMapping
    public ResponseEntity<InsuranceResponseDTO> create(@RequestBody InsuranceRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    // --- READ ALL ---
    @Operation(summary = "Get all policies", description = "Retrieves all insurance policies registered.")
    @GetMapping
    public ResponseEntity<List<InsuranceResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // --- READ ONE ---
    @Operation(summary = "Get policy by ID", description = "Retrieves a single policy details.")
    @GetMapping("/{id}")
    public ResponseEntity<InsuranceResponseDTO> getById(@Parameter(description = "MongoDB ID") @PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // --- READ ACTIVE ---
    @Operation(summary = "Get active policies", description = "Retrieves currently active policies based on date range.")
    @GetMapping("/status/active")
    public ResponseEntity<List<InsuranceResponseDTO>> getActivePolicies() {
        return ResponseEntity.ok(service.getActivePolicies());
    }

    // --- READ EXPIRING SOON ---
    @Operation(summary = "Get expiring policies", description = "Retrieves policies expiring within the next 30 days.")
    @GetMapping("/status/expiring")
    public ResponseEntity<List<InsuranceResponseDTO>> getExpiringPolicies() {
        return ResponseEntity.ok(service.getExpiringSoon());
    }

    // --- READ BY CATEGORY ---
    @Operation(summary = "Filter by Category", description = "Get policies like TERM_LIFE, HEALTH, CAR, etc.")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<InsuranceResponseDTO>> getByCategory(@PathVariable InsuranceCategory category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    // --- UPDATE ---
    @Operation(summary = "Update policy", description = "Updates an existing policy details.")
    @PutMapping("/{id}")
    public ResponseEntity<InsuranceResponseDTO> update(
            @Parameter(description = "MongoDB ID") @PathVariable String id,
            @RequestBody InsuranceRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // --- DELETE ---
    @Operation(summary = "Delete policy", description = "Removes a policy from the system.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "MongoDB ID") @PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}