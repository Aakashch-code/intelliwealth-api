package com.example.intelliwealth.wealth.networth;


import com.example.intelliwealth.wealth.asset.Asset;
import com.example.intelliwealth.wealth.asset.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.AssetsResponseDTO;
import com.example.intelliwealth.wealth.debt.Debt;
import com.example.intelliwealth.wealth.asset.AssetService;
import com.example.intelliwealth.wealth.debt.DebtResponseDTO;
import com.example.intelliwealth.wealth.debt.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/networth")
public class NetWorthController {

    @Autowired
    private NetWorthService service;

    @Autowired
    private AssetService assetService;

    @Autowired
    private DebtService debtService;

    @GetMapping
    public BigDecimal getNetWorth() {
        return service.calculateNetWorth();
    }

    @GetMapping("/debt")
    public List<DebtResponseDTO> fetchAllDebt() {
        return debtService.getAll();
    }

    @GetMapping("/asset")
    private List<AssetsResponseDTO> fetchAllAssets() {
        return assetService.getAllAssets();
    }


}
