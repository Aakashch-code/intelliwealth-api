package com.example.intelliwealth.fynix.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
class Recommendation {
    private String id;
    private String title;
    private String description;
    private String priority;
    private BigDecimal estimatedMonthlyImpact;
    private Double confidence;
    private List<String> actions;
}
