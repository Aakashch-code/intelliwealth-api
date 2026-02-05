package com.example.intelliwealth.wealth.asset.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Hidden
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

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal currentValue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAcquired;


    private Map<String, Object> attributes;
}
