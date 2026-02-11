package com.example.intelliwealth.advisor.tour;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourService {

//    private final BudgetService budgetService;
//    private final ContingencyService contingencyService;
//    private final TransactionService transactionService;
//    private final DebtService debtService;
//    private final GoalService goalService;
//
//    public TourSummaryDTO getTourSummary() {
//
//        BudgetSummaryDTO budgetSummary = budgetService.getBudgetSummary();
//        ContingencyReportDTO contingency = contingencyService.getHealthCheck();
//        SavingResponse saving = transactionService.calculateNetPosition();
//        DebtStatsDTO debtSummary = debtService.debtAmountSummary();
//        GoalStatsResponseDTO tripGoals = goalService.getGoalStats();
//
//        TourSummaryDTO dto = new TourSummaryDTO();
//        dto.setBudgetSummary(budgetSummary);
//        dto.setContingency(contingency);
//        dto.setNetPosition(saving);
//        dto.setDebtSummary(debtSummary);
//        dto.setGoalStats(tripGoals);
//
//        return dto;
//    }
}
