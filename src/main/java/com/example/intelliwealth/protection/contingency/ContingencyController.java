package com.example.intelliwealth.protection.contingency;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protection/contingency")
@RequiredArgsConstructor
@Tag(name = "Contingency Planning", description = "APIs for Emergency Fund and Financial Survival Analysis")
public class ContingencyController {

    private final ContingencyService contingencyService;

    @Operation(
            summary = "Get Financial Health Check",
            description = "Calculates your 'Burn Rate' vs 'Liquid Assets' to determine how many months you can survive without income."
    )
    @GetMapping("/health")
    public ResponseEntity<ContingencyReportDTO> getFinancialHealth() {
        // Corrected method name to match Service
        ContingencyReportDTO report = contingencyService.getHealthCheck();
        return ResponseEntity.ok(report);
    }
}