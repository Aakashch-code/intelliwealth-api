package com.example.intelliwealth.core.goal;



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
}
