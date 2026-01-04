package com.example.intelliwealth.wealth.debt.controller;

import com.example.intelliwealth.wealth.debt.service.DebtService;
import com.example.intelliwealth.wealth.debt.dto.DebtRequestDTO;
import com.example.intelliwealth.wealth.debt.dto.DebtResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
@Tag(name = "Debt Management", description = "APIs for managing user debts")
public class DebtController {

    private final DebtService service;

    @Operation(summary = "Get all debts", description = "Fetch all debts of the user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Debts fetched successfully")
    })
    @GetMapping
    public List<DebtResponseDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Get debt by ID", description = "Fetch a specific debt using its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Debt found"),
            @ApiResponse(responseCode = "404", description = "Debt not found")
    })
    @GetMapping("/{id}")
    public DebtResponseDTO getById(@PathVariable String id) {
        return service.getById(id);
    }

    @Operation(summary = "Create a new debt")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Debt created successfully",
                    content = @Content(schema = @Schema(implementation = DebtResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<DebtResponseDTO> create(
            @RequestBody DebtRequestDTO dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing debt")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Debt updated successfully"),
            @ApiResponse(responseCode = "404", description = "Debt not found")
    })
    @PutMapping("/{id}")
    public DebtResponseDTO update(
            @PathVariable String id,
            @RequestBody DebtRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete a debt")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Debt deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Debt not found")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @Operation(summary = "Get total outstanding debt amount")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total debt calculated")
    })
    @GetMapping("/total")
    public BigDecimal totalDebt() {
        return service.totalDebt();
    }
}
