package com.example.intelliwealth.wealth.asset.application.dto;

import com.example.intelliwealth.wealth.asset.domain.model.AssetCategory;
import com.example.intelliwealth.wealth.asset.domain.model.AssetCurrency;
import com.example.intelliwealth.wealth.asset.domain.model.AssetMainCategory;
import com.example.intelliwealth.wealth.asset.domain.model.AssetPriority;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class AssetsRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Main Category is required")
    private AssetMainCategory mainCategory;

    @NotNull(message = "Category is required")
    private AssetCategory category;

    @NotNull(message = "Priority is required")
    private AssetPriority priority;

    @NotNull(message = "Current Value is required")
    private BigDecimal currentValue;

    @NotNull(message = "Currency is required")
    private AssetCurrency currency;

    @NotNull(message = "Date Acquired is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;

    private Map<String, Object> attributes;
}