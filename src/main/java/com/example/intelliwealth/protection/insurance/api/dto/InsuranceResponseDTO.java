package com.example.intelliwealth.protection.insurance.api.dto;

import com.example.intelliwealth.protection.insurance.domain.model.InsuranceCategory;
import com.example.intelliwealth.protection.insurance.domain.model.InsuranceMainCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class InsuranceResponseDTO {

    private String id;
    private String name;
    private String provider;
    private InsuranceCategory category;
    private InsuranceMainCategory mainCategory;

    private BigDecimal premiumAmount;
    private BigDecimal coverageAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private Map<String, Object> attributes;
}
