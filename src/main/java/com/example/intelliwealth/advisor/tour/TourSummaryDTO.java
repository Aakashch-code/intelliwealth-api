package com.example.intelliwealth.advisor.tour;

import com.example.intelliwealth.protection.contingency.ContingencyReportDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStatsResponseDTO;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import lombok.Data;

import java.util.List;

@Data
public class TourSummaryDTO {
    private String timeframe = "The collected data is from all time.";
    private BudgetSummaryDTO budgetSummary;
    private ContingencyReportDTO contingency;
    private SavingResponse netPosition;
    private DebtStatsDTO debtSummary;
    private GoalStatsResponseDTO goalStats;
}
