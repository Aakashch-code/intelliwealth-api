package com.example.intelliwealth.service;

import com.example.intelliwealth.model.Asset;
import com.example.intelliwealth.model.Debt;
import com.example.intelliwealth.repository.AssetRepository;
import com.example.intelliwealth.repository.DebtRepository;
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
                .map(Debt::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalAssets.subtract(totalDebts);
    }



}
