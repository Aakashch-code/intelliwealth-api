package com.example.intelliwealth.dto.goal;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GoalStatsResponse {

    private long totalGoals;
    private long completedGoals;
    private BigDecimal totalTargetAmount;
    private BigDecimal totalCurrentAmount;
}
