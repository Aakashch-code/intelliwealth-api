package com.example.intelliwealth.treasury.goal.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalMinimalDTO {

    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate targetDate;


}
