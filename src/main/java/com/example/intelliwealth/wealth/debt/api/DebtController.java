package com.example.intelliwealth.wealth.debt.api;

import com.example.intelliwealth.wealth.debt.application.dto.DebtRequestDTO;
import com.example.intelliwealth.wealth.debt.application.dto.DebtResponseDTO;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import com.example.intelliwealth.wealth.debt.application.service.DebtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
@Tag(name = "Debt Management", description = "APIs for managing user debts")
public class DebtController {

    private final DebtService service;

    @Operation(summary = "Get all debts")
    @GetMapping
    public List<DebtResponseDTO> getAll() {

        return service.getAll();
    }

    @Operation(summary = "Get debt by ID")
    @GetMapping("/{id}")
    public DebtResponseDTO getById(@PathVariable String id) {

        return service.getById(id);
    }

    @Operation(summary = "Create debt")
    @PostMapping
    public ResponseEntity<DebtResponseDTO> create(
            @Valid @RequestBody DebtRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @Operation(summary = "Update debt")
    @PutMapping("/{id}")
    public DebtResponseDTO update(
            @PathVariable String id,
            @Valid @RequestBody DebtRequestDTO dto) {

        return service.update(id, dto);
    }

    @Operation(summary = "Delete debt")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {

        service.delete(id);
    }

    @Operation(summary = "Get debt statistics")
    @GetMapping("/stats")
    public DebtStatsDTO stats() {

        return service.debtAmountSummary();
    }

    @Operation(summary = "Get upcoming EMI report")
    @GetMapping("/emi")
    public Map<String, BigDecimal> getNextFiveMonthsEMIs() {

        return service.getNextFiveMonthsEMIs();
    }
}