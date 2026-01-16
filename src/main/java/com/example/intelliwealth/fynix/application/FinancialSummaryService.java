package com.example.intelliwealth.fynix.application;

import com.example.intelliwealth.core.goal.GoalResponseDTO;
import com.example.intelliwealth.core.goal.GoalService;
import com.example.intelliwealth.core.subscription.SubscriptionService;
import com.example.intelliwealth.core.transaction.TransactionService;
import com.example.intelliwealth.fynix.api.dto.FinancialSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialSummaryService {

    private final TransactionService transactionService;
    private final SubscriptionService subscriptionService;
    private final GoalService goalService;

    public FinancialSummary buildSummary() {

        FinancialSummary s = new FinancialSummary();

        var txs = transactionService.getAllTransactions();
        var subs = subscriptionService.getAllSubscriptions();
        var goals = goalService.getAllGoal();

        // --- Income ---
        double totalIncome = txs.stream()
                .filter(t -> "INCOME".equalsIgnoreCase(t.getType()))
                .mapToDouble(t -> t.getAmount().doubleValue())
                .sum();

        // --- Expense ---
        double totalExpense = txs.stream()
                .filter(t -> "EXPENSE".equalsIgnoreCase(t.getType()))
                .mapToDouble(t -> t.getAmount().doubleValue())
                .sum();

        // --- Subscriptions ---
        double subscriptionCost = subs.stream()
                .mapToDouble(su -> su.getAmount().doubleValue())
                .sum();

        // --- Goal Progress & Context ---
        double goalProgress = 0.0;

        if (!goals.isEmpty()) {
            // 1. Calculate Average Progress
            goalProgress = goals.stream()
                    .mapToDouble(this::calculateGoalProgress)
                    .average()
                    .orElse(0);

            // 2. Map Goal Details for AI Context
            List<FinancialSummary.GoalContext> goalContexts = goals.stream()
                    .map(g -> {
                        FinancialSummary.GoalContext ctx = new FinancialSummary.GoalContext();
                        // Handle potential nulls safely
                        ctx.setId(String.valueOf(g.getId()));
                        ctx.setName(g.getName() != null ? g.getName() : "Unnamed Goal");
                        ctx.setCurrent(g.getCurrentAmount() != null ? g.getCurrentAmount().doubleValue() : 0.0);
                        ctx.setTarget(g.getTargetAmount() != null ? g.getTargetAmount().doubleValue() : 0.0);
                        return ctx;
                    })
                    .toList();

            s.setTopGoals(goalContexts);
        }

        // --- Fill summary ---
        s.setTotalIncome(totalIncome);
        s.setTotalExpense(totalExpense);
        s.setNetWorth(totalIncome - totalExpense);
        s.setMonthlySubscriptionCost(subscriptionCost);
        s.setActiveSubscriptions(subs.size());
        s.setTotalGoals(goals.size());
        s.setGoalProgress(goalProgress);

        return s;
    }

    private double calculateGoalProgress(GoalResponseDTO goal) {
        if (goal.getTargetAmount() == null ||
                goal.getTargetAmount().doubleValue() == 0) {
            return 0;
        }

        return (goal.getCurrentAmount().doubleValue()
                / goal.getTargetAmount().doubleValue()) * 100;
    }
}