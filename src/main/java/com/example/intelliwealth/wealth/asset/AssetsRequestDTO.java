package com.example.intelliwealth.wealth.asset;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class AssetsRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private AssetMainCategory mainCategory;

    @NotNull
    private AssetCategory category;

    @NotNull
    private BigDecimal currentValue;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;

    private Map<String, Object> attributes;
}
