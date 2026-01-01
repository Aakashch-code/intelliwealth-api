package com.example.intelliwealth.protection.insurance;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class InsuranceRequestDTO {

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
