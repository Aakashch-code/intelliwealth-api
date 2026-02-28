package com.example.intelliwealth.wealth.asset.api;

import com.example.intelliwealth.wealth.asset.infrastructure.export.AssetExportService;
import com.example.intelliwealth.wealth.asset.application.service.AssetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RequestMapping("/api/asset/export")
@RequiredArgsConstructor
@RestController
@Tag(name = "Asset Management", description = "Operations related to managing financial assets")
public class AssetExportPdfController {

    private final AssetService assetService;
    private final AssetExportService assetExportService;

    @GetMapping("/pdf")
    public void exportGoalToPdf(HttpServletResponse response)
            throws IOException {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=asset_" +
                        LocalDate.now() + ".pdf"
        );

        assetExportService.generate(
                response,
                assetService.getAllAssets()
        );
    }
}

