package com.example.intelliwealth.fynix.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class Recommendation {
    private String id;
    private String title;
    private String description;
    private String priority;
    private BigDecimal estimatedMonthlyImpact;
    private Double confidence;
    private List<String> actions;
}
