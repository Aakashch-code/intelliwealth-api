package com.example.intelliwealth.wealth.debt.domain.model;

import com.example.intelliwealth.wealth.debt.application.dto.DebtStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Hidden
@Document(collection = "debts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debt {

    @Id
    private String id;

    private UUID userId;
    private String name;
    private String creditor;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal totalAmount;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal outstandingAmount;

    private DebtCategory category;
    private DebtMainCategory mainCategory;

    private Map<String, Object> attributes;
    private DebtStatus status = DebtStatus.ACTIVE;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
}
