package com.example.intelliwealth.wealth.asset.dto;

import com.example.intelliwealth.wealth.asset.domain.AssetCategory;
import com.example.intelliwealth.wealth.asset.domain.AssetMainCategory;
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

    @NotNull(message = "Current Value is required")
    private BigDecimal currentValue;

    @NotNull(message = "Date Acquired is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;

    // This allows the user to send dynamic fields
    private Map<String, Object> attributes;
}