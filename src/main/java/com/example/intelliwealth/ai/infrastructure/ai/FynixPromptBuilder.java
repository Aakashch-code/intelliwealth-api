package com.example.intelliwealth.ai.infrastructure.ai;

import com.example.intelliwealth.ai.api.dto.FinancialSummary;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class FynixPromptBuilder {

    public String buildPrompt(String query, FinancialSummary s) {

        // 1. Format Goals into a string list so the AI can read them
        String goalContext = s.getTopGoals().isEmpty()
                ? "No active goals."
                : s.getTopGoals().stream()
                .map(g -> String.format("- ID: %s, Name: %s, Current: %.2f, Target: %.2f",
                        g.getId(), g.getName(), g.getCurrent(), g.getTarget()))
                .collect(Collectors.joining("\n"));

        return """
        You are Fynix, a strict financial advisor AI.
        
        User Question:
        %s

        Financial Data:
        - Income: %.2f
        - Expense: %.2f
        - Net Worth: %.2f
        - Active Subscriptions: %d (Cost: %.2f)
        - Active Goals:
        %s

        IMPORTANT RULES:
        1. Return ONLY valid JSON.
        2. "title" in recommendations is mandatory.
        3. "goalId" must match the IDs provided in the Financial Data.
        
        Expected JSON schema:
        {
          "summary": "Brief executive summary string",
          "recommendations": [
            {
              "title": "Actionable Title", 
              "description": "Specific details",
              "priority": "LOW | MEDIUM | HIGH",
              "estimatedMonthlyImpact": number,
              "actions": ["step 1", "step 2"]
            }
          ],
          "keyMetrics": {
            "income": number,
            "expense": number,
            "netWorth": number
          },
          "goalAlignment": [
            {
              "goalId": "string (copy exact ID from data)",
              "goalName": "string (copy exact Name from data)",
              "alignment": "ON_TRACK | NEEDS_ATTENTION | AT_RISK",
              "notes": "Advice on this specific goal"
            }
          ]
        }
        """.formatted(
                query,
                s.getTotalIncome(),
                s.getTotalExpense(),
                s.getNetWorth(),
                s.getActiveSubscriptions(),
                s.getMonthlySubscriptionCost(),
                goalContext // <--- Injecting the specific goal details here
        );
    }
}