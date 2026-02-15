package com.example.intelliwealth.protection.insurance.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class InsuranceCategorySummary {
    private int policyCount;
    private BigDecimal totalCoverageAmount; // Important for "Am I underinsured?"
    private BigDecimal totalAnnualPremium;  // Important for "Can I afford this?"
    private List<String> policyNames;       // Just the names, e.g., "HDFC Ergo Optima"
    private LocalDate nextRenewalDate;   // e.g., 2024-12-01
    private long daysToNextRenewal;      // e.g., 15 (Critical for "Urgency")
    private String renewalStatus;
}