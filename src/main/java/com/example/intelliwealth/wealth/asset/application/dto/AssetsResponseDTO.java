package com.example.intelliwealth.wealth.asset.application.dto;

import com.example.intelliwealth.wealth.asset.domain.model.AssetCategory;
import com.example.intelliwealth.wealth.asset.domain.model.AssetCurrency;
import com.example.intelliwealth.wealth.asset.domain.model.AssetMainCategory;
import com.example.intelliwealth.wealth.asset.domain.model.AssetPriority;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Data
public class AssetsResponseDTO {

    private String id;
    private String name;
    private AssetMainCategory mainCategory;
    private AssetCategory category;
    private AssetPriority priority;
    private BigDecimal currentValue;
    private AssetCurrency currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;

    private Map<String, Object> attributes = new HashMap<>();

    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        return this.attributes;
    }
}