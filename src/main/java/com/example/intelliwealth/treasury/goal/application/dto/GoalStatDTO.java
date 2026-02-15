package com.example.intelliwealth.treasury.goal.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalStatDTO {
    private Long totalGoals;
    private Long completedGoals;
    private BigDecimal totalTarget;
    private BigDecimal totalCurrent;
    private Long totalMonthlyRequired;

    public GoalStatDTO(Long totalGoals, Long completedGoals, BigDecimal totalTarget, BigDecimal totalCurrent) {
        this.totalGoals = totalGoals;
        this.completedGoals = completedGoals;
        this.totalTarget = totalTarget;
        this.totalCurrent = totalCurrent;
    }
}