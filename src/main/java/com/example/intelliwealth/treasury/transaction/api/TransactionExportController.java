package com.example.intelliwealth.treasury.transaction.api;

import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import com.example.intelliwealth.treasury.transaction.infrastructure.export.TransactionExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions/export")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Financial transaction management")
public class TransactionExportController {

    private final TransactionService transactionService;
    private final TransactionExportService transactionExportService;

    @Operation(summary = "Export transactions as PDF")
    @GetMapping("/pdf")
    public void exportTransactionsToPdf(
            HttpServletResponse response,
            Pageable pageable) throws IOException {

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=transactions_" +
                        LocalDate.now() + ".pdf"
        );

        transactionExportService.generate(
                response,
                transactionService.getTransactions(null, pageable)
        );
    }
}