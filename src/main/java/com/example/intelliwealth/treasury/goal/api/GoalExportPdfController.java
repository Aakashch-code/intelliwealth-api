package com.example.intelliwealth.treasury.goal.api;
import com.example.intelliwealth.treasury.goal.application.service.GoalExportService;
import com.example.intelliwealth.treasury.goal.application.service.GoalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RequestMapping("/api/goal/export")
@RequiredArgsConstructor
@RestController
@Tag(name = "Goal Management", description = "APIs for managing financial goals, tracking progress, and statistics")
public class GoalExportPdfController {

    private final GoalService goalService;
    private final GoalExportService goalExportService;

    @GetMapping("/pdf")
    public void exportGoalToPdf(HttpServletResponse response)
            throws IOException {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=goal_" +
                        LocalDate.now() + ".pdf"
        );

        goalExportService.generate(
                response,
                goalService.getAllGoal()
        );
    }
}

