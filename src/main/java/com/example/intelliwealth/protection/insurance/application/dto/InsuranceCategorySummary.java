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
    private BigDecimal totalCoverageAmount;
    private BigDecimal totalAnnualPremium;
    private List<String> policyNames;
    private LocalDate nextRenewalDate;
    private long daysToNextRenewal;
    private String renewalStatus;
}