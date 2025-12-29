package com.example.intelliwealth.wealth.debt;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
@Document(collection = "debts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debt {

    @Id
    private String id;

    private String name;
    private String creditor;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal totalAmount;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal outstandingAmount;

    private DebtCategory category;
    private DebtMainCategory mainCategory;

    private Map<String, Object> attributes;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dueDate;
}
