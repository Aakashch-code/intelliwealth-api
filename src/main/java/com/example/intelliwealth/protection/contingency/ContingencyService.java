package com.example.intelliwealth.protection.contingency;

import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import com.example.intelliwealth.treasury.subscription.application.service.SubscriptionService;
import com.example.intelliwealth.wealth.asset.application.dto.AssetsResponseDTO;
import com.example.intelliwealth.wealth.debt.application.service.DebtService;
import com.example.intelliwealth.wealth.asset.application.service.AssetService;
import com.example.intelliwealth.wealth.asset.domain.model.AssetCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Service
public class ContingencyService {

    private final TransactionService transactionService;
    private final SubscriptionService subscriptionService;
    private final DebtService debtService;
    private final AssetService assetService;

    private static final BigDecimal SIX_MONTHS = new BigDecimal("6");

    public ContingencyService(TransactionService transactionService,
                              SubscriptionService subscriptionService,
                              DebtService debtService,
                              AssetService assetService) {
        this.transactionService = transactionService;
        this.subscriptionService = subscriptionService;
        this.debtService = debtService;
        this.assetService = assetService;
    }

    public ContingencyReportDTO getHealthCheck() {
        // 1. CALCULATE MONTHLY BURN (Expenses + Subs + EMIs)
        // Assuming your services return BigDecimal.ZERO if no data exists
        BigDecimal avgExpenses = transactionService.getMonthlyAverageExpense(3);
        BigDecimal activeSubs = subscriptionService.getTotalMonthlySubscriptions();
        BigDecimal currentDebtEMI = debtService.getTotalMonthlyEMIs();

        BigDecimal monthlyBurn = avgExpenses.add(activeSubs).add(currentDebtEMI);

        // Safety check to avoid division by zero
        if (monthlyBurn.compareTo(BigDecimal.ZERO) == 0) {
            return new ContingencyReportDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "UNKNOWN");
        }

        // 2. CALCULATE LIQUID WEALTH (Using your Categories)
        Page<AssetsResponseDTO> allAssets = assetService.getAllAssets(Pageable.unpaged());
        BigDecimal liquidWealth = calculateLiquidWealth(allAssets);

        // 3. CALCULATE RUNWAY (Liquid Wealth / Monthly Burn)
        BigDecimal survivalMonths = liquidWealth.divide(monthlyBurn, 1, RoundingMode.HALF_DOWN);

        // 4. DETERMINE STATUS
        String status = determineStatus(survivalMonths);

        // 5. CALCULATE GAP
        BigDecimal targetSafetyNet = monthlyBurn.multiply(SIX_MONTHS);
        BigDecimal gap = targetSafetyNet.subtract(liquidWealth);
        if (gap.compareTo(BigDecimal.ZERO) < 0) gap = BigDecimal.ZERO;

        return new ContingencyReportDTO(monthlyBurn, liquidWealth, survivalMonths, gap,status);
    }

    public BigDecimal calculateLiquidWealth(Page<AssetsResponseDTO> assets) {

        BigDecimal totalLiquid = BigDecimal.ZERO;

        for (AssetsResponseDTO asset : assets) {

            BigDecimal value = asset.getCurrentValue();

            if (value == null) continue;

            if (asset.getCategory() == AssetCategory.CASH ||
                    asset.getCategory() == AssetCategory.FIXED_INCOME) {

                totalLiquid = totalLiquid.add(value);

            }
            else if (asset.getCategory() == AssetCategory.MUTUAL_FUND ||
                    asset.getCategory() == AssetCategory.EQUITY) {

                // 80% liquidity
                BigDecimal adjusted = value.multiply(new BigDecimal("0.8"));
                totalLiquid = totalLiquid.add(adjusted);

            }
            else if (asset.getCategory() == AssetCategory.CRYPTO) {

                // 50% liquidity
                BigDecimal adjusted = value.multiply(new BigDecimal("0.5"));
                totalLiquid = totalLiquid.add(adjusted);
            }
            // REAL_ESTATE, VEHICLE = ignored
        }

        return totalLiquid;
    }


    public String determineStatus(BigDecimal months) {
        if (months.compareTo(new BigDecimal("3")) < 0) return "DANGER";
        if (months.compareTo(new BigDecimal("6")) < 0) return "WARNING";
        return "SAFE";
    }
}