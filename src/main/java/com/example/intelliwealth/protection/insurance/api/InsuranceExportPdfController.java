package com.example.intelliwealth.protection.insurance.api;

import com.example.intelliwealth.protection.insurance.application.service.InsuranceService;
import com.example.intelliwealth.protection.insurance.infrastructure.export.InsuranceExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/insurance/export")
@RequiredArgsConstructor
@Tag(
        name = "Insurance Management",
        description = "APIs for managing user insurance policies (Life, Health, General)"
)
public class InsuranceExportPdfController {

    private final InsuranceService insuranceService;
    private final InsuranceExportService insuranceExportService;

    @Operation(summary = "Export insurance policies as PDF")
    @GetMapping("/pdf")
    public void exportGoalToPdf(HttpServletResponse response)
            throws IOException {

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=insurance_" +
                        LocalDate.now() + ".pdf"
        );

        insuranceExportService.generate(
                response,
                insuranceService.getActivePolicies()
        );
    }
}