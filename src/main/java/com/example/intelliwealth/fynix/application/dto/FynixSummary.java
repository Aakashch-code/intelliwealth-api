package com.example.intelliwealth.fynix.application.dto;

import com.example.intelliwealth.protection.contingency.ContingencyReportDTO;
import com.example.intelliwealth.protection.insurance.application.dto.InsuranceCategorySummary;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStatDTO;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionStatDTO;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import com.example.intelliwealth.wealth.networth.NetWorthResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder; // Added Builder for easier object creation
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Decimal128;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder // Useful for aggregation
@NoArgsConstructor
@AllArgsConstructor
public class FynixSummary {

    private String period;

    // Treasury & Wealth Data
    private BudgetSummaryDTO budgetStats;
    private GoalStatDTO goalStats;
    private SubscriptionStatDTO subscriptionStat;
    private SavingResponse transactionStats;
    private DebtStatsDTO debtStats;
    private NetWorthResponseDTO netWorthStats;
    private ContingencyReportDTO contingencyStats;
    // Protection Data (Specific for LLM Context)
    private InsuranceCategorySummary healthInsuranceSummary;
    private InsuranceCategorySummary lifeInsuranceSummary;
}