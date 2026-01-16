package com.example.intelliwealth.wealth.networth;

import com.example.intelliwealth.authentication.application.SecuredService;
import com.example.intelliwealth.wealth.asset.domain.repository.AssetRepository;
import com.example.intelliwealth.wealth.debt.domain.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NetWorthService extends SecuredService {

    private final AssetRepository assetRepository;
    private final DebtRepository debtRepository;

    public NetWorthResponseDTO calculateNetWorth() {
        var userId = currentUserId();

        BigDecimal totalAssets = sumAssets(userId);
        BigDecimal totalDebts = sumDebts(userId);

        return new NetWorthResponseDTO(
                totalAssets,
                totalDebts,
                totalAssets.subtract(totalDebts)
        );
    }

    private BigDecimal sumAssets(UUID userId) {
        return assetRepository.findAllByUserId(userId)
                .stream()
                .map(a -> a.getCurrentValue() == null ? BigDecimal.ZERO : a.getCurrentValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumDebts(UUID userId) {
        return debtRepository.findAllByUserId(userId)
                .stream()
                .map(d -> d.getOutstandingAmount() == null ? BigDecimal.ZERO : d.getOutstandingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

