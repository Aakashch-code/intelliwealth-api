package com.example.intelliwealth.advisor.tour;

import com.example.intelliwealth.protection.contingency.ContingencyReportDTO;
import com.example.intelliwealth.protection.contingency.ContingencyService;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.budget.application.service.BudgetService;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStatsResponseDTO;
import com.example.intelliwealth.treasury.goal.application.service.GoalService;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import com.example.intelliwealth.wealth.debt.application.DebtService;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourService {

    private final BudgetService budgetService;
    private final ContingencyService contingencyService;
    private final TransactionService transactionService;
    private final DebtService debtService;
    private final GoalService goalService;

    public TourSummaryDTO getTourSummary() {

        BudgetSummaryDTO budgetSummary = budgetService.getBudgetSummary();
        ContingencyReportDTO contingency = contingencyService.getHealthCheck();
        SavingResponse saving = transactionService.calculateNetPosition();
        DebtStatsDTO debtSummary = debtService.debtAmountSummary();
        GoalStatsResponseDTO tripGoals = goalService.getGoalStats();

        TourSummaryDTO dto = new TourSummaryDTO();
        dto.setBudgetSummary(budgetSummary);
        dto.setContingency(contingency);
        dto.setSaving(saving);
        dto.setDebtSummary(debtSummary);
        dto.setGoalStats(tripGoals);

        return dto;
    }
}
