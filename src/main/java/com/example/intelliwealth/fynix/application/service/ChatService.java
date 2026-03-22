package com.example.intelliwealth.fynix.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.fynix.application.dto.FynixSummary;

import com.example.intelliwealth.protection.contingency.ContingencyReportDTO;
import com.example.intelliwealth.protection.contingency.ContingencyService;
import com.example.intelliwealth.protection.insurance.application.dto.InsuranceResponseDTO;
import com.example.intelliwealth.protection.insurance.application.service.InsuranceService;
import com.example.intelliwealth.protection.insurance.application.service.InsuranceSummaryService;
import com.example.intelliwealth.protection.insurance.domain.model.InsuranceCategory;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummary;
import com.example.intelliwealth.treasury.budget.application.service.BudgetService;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStat;
import com.example.intelliwealth.treasury.goal.application.service.GoalService;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionStat;
import com.example.intelliwealth.treasury.subscription.application.service.SubscriptionService;
import com.example.intelliwealth.treasury.transaction.application.dto.SavingResponse;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import com.example.intelliwealth.wealth.asset.application.service.AssetService;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import com.example.intelliwealth.wealth.debt.application.service.DebtService;
import com.example.intelliwealth.wealth.networth.NetWorthResponseDTO;
import com.example.intelliwealth.wealth.networth.NetWorthService;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService extends SecuredService {

    private final InsuranceService insuranceService;
    private final GoalService goalService;
    private final BudgetService budgetService;
    private final SubscriptionService subscriptionService;
    private final TransactionService transactionService;
    private final AssetService assetService;
    private final DebtService debtService;
    private final NetWorthService netWorthService;
    private final ContingencyService contingencyService;
    private final InsuranceSummaryService insuranceSummaryService;


    public FynixSummary generateContext() {

        String note = "The provided data covers all time periods.";
        //Treasury
        BudgetSummary budget = budgetService.getBudgetSummary();
        GoalStat goal = goalService.getGoalStats();
        SubscriptionStat subscription = subscriptionService.getStats();
        SavingResponse saving = transactionService.calculateNetPosition();

        //Wealth
        Decimal128 asset = assetService.allAssetsAmount();
        DebtStatsDTO debt = debtService.debtAmountSummary();
        NetWorthResponseDTO netWorth = netWorthService.calculateNetWorth();

        //Protection
        ContingencyReportDTO contingency = contingencyService.getHealthCheck();
        List<InsuranceResponseDTO> allPolicies = insuranceService.getActivePolicies();
        Map<InsuranceCategory, List<InsuranceResponseDTO>> grouped = allPolicies.stream()
                .collect(Collectors.groupingBy(InsuranceResponseDTO::getCategory));


        // 3. Build the summaries
        return FynixSummary.builder()
                .period(note)
                .healthInsuranceSummary(
                        insuranceSummaryService.summarize(grouped.get(InsuranceCategory.HEALTH))
                )
                .lifeInsuranceSummary(
                        insuranceSummaryService.summarize(grouped.get(InsuranceCategory.LIFE))
                )

                .budgetStats(budget)
                .goalStats(goal)
                .subscriptionStat(subscription)
                .transactionStats(saving)
                .debtStats(debt)
                .netWorthStats(netWorth)
                .contingencyStats(contingency)
                .build();
    }


}