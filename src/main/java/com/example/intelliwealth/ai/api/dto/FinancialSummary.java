package com.example.intelliwealth.ai.api.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class FinancialSummary {

    private double totalIncome;
    private double totalExpense;
    private double netWorth;

    private int activeSubscriptions;
    private double monthlySubscriptionCost;

    private int totalGoals;
    private double goalProgress; // Average %

    // New field to carry context to the AI
    private List<GoalContext> topGoals = new ArrayList<>();

    @Data
    public static class GoalContext {
        private String id;
        private String name;
        private double current;
        private double target;
    }
}