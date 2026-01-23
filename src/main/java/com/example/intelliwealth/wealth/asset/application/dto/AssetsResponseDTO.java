package com.example.intelliwealth.wealth.asset.application.dto;

import com.example.intelliwealth.wealth.asset.domain.model.AssetCategory;
import com.example.intelliwealth.wealth.asset.domain.model.AssetMainCategory;
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
    private BigDecimal currentValue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;

    // Initialize logic
    private Map<String, Object> attributes = new HashMap<>();

    // --- ADD THIS MANUAL GETTER ---
    // Lombok will see this and will NOT generate its own getter.
    // This protects the frontend from ever receiving "null".
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        return this.attributes;
    }
}