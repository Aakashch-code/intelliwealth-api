package com.example.intelliwealth.protection.insurance.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Document(collection = "insurance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance {

    @Id
    private String id;

    private UUID userId;

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
