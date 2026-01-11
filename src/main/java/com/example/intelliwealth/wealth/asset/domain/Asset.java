package com.example.intelliwealth.wealth.asset.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "assets")
public class Asset {

    @Id
    private String id;

    private UUID userId;
    private String name;

    private AssetMainCategory mainCategory;
    private AssetCategory category;

    private BigDecimal currentValue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;


    private Map<String, Object> attributes;
}
