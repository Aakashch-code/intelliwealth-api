package com.example.intelliwealth.wealth.networth;

import com.example.intelliwealth.wealth.asset.repository.AssetRepository;
import com.example.intelliwealth.wealth.debt.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class NetWorthService {

    private final AssetRepository assetRepository;
    private final DebtRepository debtRepository;

    public NetWorthResponseDTO calculateNetWorth() {

        BigDecimal totalAssets = assetRepository.findAll()
                .stream()
                .map(a -> a.getCurrentValue() == null ? BigDecimal.ZERO : a.getCurrentValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDebts = debtRepository.findAll()
                .stream()
                .map(d -> d.getOutstandingAmount() == null ? BigDecimal.ZERO : d.getOutstandingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new NetWorthResponseDTO(
                totalAssets,
                totalDebts,
                totalAssets.subtract(totalDebts)
        );
    }
}

