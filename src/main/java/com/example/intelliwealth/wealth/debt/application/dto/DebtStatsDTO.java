package com.example.intelliwealth.wealth.debt.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

public class DebtStatsDTO {

    private Decimal128 totalDebtAmount;
    private BigDecimal totalOutstandingAmount;
}
