package com.example.intelliwealth.protection.contingency;

import com.example.intelliwealth.core.transaction.TransactionService;
import com.example.intelliwealth.core.subscription.SubscriptionService;
import com.example.intelliwealth.wealth.asset.dto.AssetsResponseDTO;
import com.example.intelliwealth.wealth.debt.service.DebtService;
import com.example.intelliwealth.wealth.asset.service.AssetService;
import com.example.intelliwealth.wealth.asset.domain.AssetCategory;
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
        BigDecimal avgExpenses = transactionService.getAverageMonthlyExpense(3);
        BigDecimal activeSubs = subscriptionService.getTotalMonthlySubscriptions();
        BigDecimal currentDebtEMI = debtService.getTotalMonthlyEMIs();

        BigDecimal monthlyBurn = avgExpenses.add(activeSubs).add(currentDebtEMI);

        // Safety check to avoid division by zero
        if (monthlyBurn.compareTo(BigDecimal.ZERO) == 0) {
            return new ContingencyReportDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "UNKNOWN");
        }

        // 2. CALCULATE LIQUID WEALTH (Using your Categories)
        List<AssetsResponseDTO> allAssets = assetService.getAllAssets();
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

    private BigDecimal calculateLiquidWealth(List<AssetsResponseDTO> assets) {
        BigDecimal totalLiquid = BigDecimal.ZERO;

        for (AssetsResponseDTO asset : assets) {
            BigDecimal value = asset.getCurrentValue(); // Assuming this field exists
            if (value == null) continue;

            // APPLY LIQUIDITY LOGIC BASED ON YOUR ENUMS
            if (asset.getCategory() == AssetCategory.CASH ||
                    asset.getCategory() == AssetCategory.FIXED_INCOME) {
                totalLiquid = totalLiquid.add(value); // 100% Value
            }
            else if (asset.getCategory() == AssetCategory.MUTUAL_FUND ||
                    asset.getCategory() == AssetCategory.EQUITY) {
                // Conservative approach: Count only 80% due to market volatility
                totalLiquid = totalLiquid.add(value.multiply(new BigDecimal("0.8")));
            }
            else if (asset.getCategory() == AssetCategory.CRYPTO) {
                // High risk: Count only 50%
                totalLiquid = totalLiquid.add(value.multiply(new BigDecimal("0.5")));
            }
            // REAL_ESTATE and VEHICLE are ignored (0% liquidity)
        }
        return totalLiquid;
    }

    private String determineStatus(BigDecimal months) {
        if (months.compareTo(new BigDecimal("3")) < 0) return "DANGER";
        if (months.compareTo(new BigDecimal("6")) < 0) return "WARNING";
        return "SAFE";
    }
}