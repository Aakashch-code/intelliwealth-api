package com.example.intelliwealth.wealth.debt.api;

import com.example.intelliwealth.wealth.debt.application.service.DebtService;
import com.example.intelliwealth.wealth.debt.infrastructure.export.DebtExportService;
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
@RequestMapping("/api/debt/export")
@RequiredArgsConstructor
@Tag(name = "Debt Management", description = "APIs for managing user debts")
public class DebtExportPdfController {

    private final DebtService debtService;
    private final DebtExportService debtExportService;

    @Operation(summary = "Export debts as PDF")
    @GetMapping("/pdf")
    public void exportGoalToPdf(HttpServletResponse response)
            throws IOException {

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=debt_" +
                        LocalDate.now() + ".pdf"
        );

        debtExportService.generate(
                response,
                debtService.getAll()
        );
    }
}