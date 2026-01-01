package com.example.intelliwealth.wealth.networth;

import com.example.intelliwealth.wealth.asset.Asset;
import com.example.intelliwealth.wealth.debt.Debt;
import com.example.intelliwealth.wealth.asset.AssetRepository;
import com.example.intelliwealth.wealth.debt.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class NetWorthService {

    @Autowired
    private AssetRepository asset;
    @Autowired
    private DebtRepository debt;

    public BigDecimal calculateNetWorth() {
        BigDecimal totalAssets = asset.findAll().stream()
                .map(Asset::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebts = debt.findAll().stream()
                .map(Debt::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAssets.subtract(totalDebts);
    }



}
