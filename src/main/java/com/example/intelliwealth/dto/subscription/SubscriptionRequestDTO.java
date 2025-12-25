package com.example.intelliwealth.dto.subscription;

import io.swagger.v3.oas.annotations.media.Schema; // Import this
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Details required to create a new subscription")
public class SubscriptionRequestDTO {

    @Schema(description = "Name of the subscription service", example = "Spotify Premium")
    private String title;

    @Schema(description = "Cost of the subscription", example = "199.00")
    private BigDecimal amount;

    @Schema(description = "Billing frequency", example = "Monthly")
    private String billingCycle;

    @Schema(description = "Category of the expense", example = "Entertainment")
    private String category;

    @Schema(description = "Date of the next payment", example = "2025-12-25")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nextRecurrence;

    @Schema(description = "Status of the subscription", example = "true")
    private boolean isActive = true;
}