package com.example.intelliwealth.treasury.subscription.api;

import com.example.intelliwealth.treasury.subscription.application.service.SubscriptionExportService;
import com.example.intelliwealth.treasury.subscription.application.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RequestMapping("/api/subscription/export")
@RequiredArgsConstructor
@RestController
@Tag(name = "Subscription Management", description = "Manage user subscriptions")
public class SubscriptionExportPdfController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionExportService subscriptionExportService;

    @GetMapping("/pdf")
    public void exportGoalToPdf(HttpServletResponse response)
            throws IOException {

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=subscription_" +
                        LocalDate.now() + ".pdf"
        );

        subscriptionExportService.generate(
                response,
                subscriptionService.getAllSubscriptions()
        );
    }
}

