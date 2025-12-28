package com.example.intelliwealth.wealth.asset;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AssetsResponseDTO {

    private String id;
    private String name;
    private AssetMainCategory mainCategory;
    private AssetCategory category;
    private BigDecimal currentValue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;
}
