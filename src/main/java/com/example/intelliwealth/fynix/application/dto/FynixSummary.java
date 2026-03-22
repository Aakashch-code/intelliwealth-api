package com.example.intelliwealth.fynix.application.dto;

import com.example.intelliwealth.protection.contingency.ContingencyReportDTO;
import com.example.intelliwealth.protection.insurance.application.dto.InsuranceCategorySummary;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummary;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStat;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionStat;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import com.example.intelliwealth.wealth.networth.NetWorthResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FynixSummary {

    private String period;

    private BudgetSummary budgetStats;
    private GoalStat goalStats;
    private SubscriptionStat subscriptionStat;
    private SavingResponse transactionStats;
    private DebtStatsDTO debtStats;
    private NetWorthResponseDTO netWorthStats;
    private ContingencyReportDTO contingencyStats;
    private InsuranceCategorySummary healthInsuranceSummary;
    private InsuranceCategorySummary lifeInsuranceSummary;
}