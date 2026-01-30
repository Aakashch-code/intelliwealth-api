package com.example.intelliwealth.treasury.budget.api;

import com.example.intelliwealth.treasury.budget.application.service.BudgetExportService;
import com.example.intelliwealth.treasury.budget.application.service.BudgetService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/api/budget/export")
@RequiredArgsConstructor
@RestController
public class BudgetExportController {

    private final BudgetService budgetService;
    private final BudgetExportService budgetExportService;

    @GetMapping("/pdf")
    public void exportBudgetsToPdf(HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=budget_" +
                        LocalDate.now() + ".pdf"
        );

        budgetExportService.generate(
                response,
                budgetService.getAllBudgets()
        );
    }
}
