package com.example.intelliwealth.treasury.goal.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GoalStatsResponseDTO {
    private long totalGoals;
    private long completedGoals;
    private BigDecimal totalTargetAmount;
    private BigDecimal totalCurrentAmount;
    private long totalMonthlyRequired;
}