package com.example.intelliwealth.protection.insurance.dto;

import com.example.intelliwealth.protection.insurance.domain.InsuranceCategory;
import com.example.intelliwealth.protection.insurance.domain.InsuranceMainCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Schema(description = "Payload for creating or updating an Insurance Policy")
public class InsuranceRequestDTO {

    @Schema(example = "HDFC Life Click 2 Protect", description = "Name of the policy")
    private String name;

    @Schema(example = "HDFC Life", description = "Insurance Provider Name")
    private String provider;

    @Schema(description = "Specific insurance type")
    private InsuranceCategory category;

    @Schema(description = "Broad category (LIFE, GENERAL, HEALTH)")
    private InsuranceMainCategory mainCategory;

    @Schema(example = "15000.00", description = "Yearly premium amount")
    private BigDecimal premiumAmount;

    @Schema(example = "10000000.00", description = "Total coverage/sum assured")
    private BigDecimal coverageAmount;

    @Schema(example = "2024-01-01", description = "Policy start date")
    private LocalDate startDate;

    @Schema(example = "2025-01-01", description = "Policy renewal/end date")
    private LocalDate endDate;

    @Schema(description = "Dynamic attributes based on category (e.g., vehicleNumber for Car Insurance)")
    private Map<String, Object> attributes;
}