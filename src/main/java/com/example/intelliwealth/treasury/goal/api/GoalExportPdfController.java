package com.example.intelliwealth.treasury.goal.api;

import com.example.intelliwealth.treasury.goal.infrastructure.export.GoalExportService;
import com.example.intelliwealth.treasury.goal.application.service.GoalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
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

    @Operation(
            summary = "Export goals as PDF",
            description = "Downloads a PDF file containing the user's financial goals with pagination support."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "PDF exported successfully",
                    content = @Content(mediaType = "application/pdf")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while generating PDF",
                    content = @Content
            )
    })
    @GetMapping("/pdf")
    public void exportGoalToPdf(
            HttpServletResponse response,

            @Parameter(
                    description = "Pagination information (page, size, sort)"
            )
            Pageable pageable
    ) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=goal_" +
                        LocalDate.now() + ".pdf"
        );

        goalExportService.generate(
                response,
                goalService.getAllGoal(pageable)
        );
    }
}
