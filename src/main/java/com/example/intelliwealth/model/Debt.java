package com.example.intelliwealth.model;

import com.example.intelliwealth.config.CurrencySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Debt {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    private String name;
    private String creditor;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal initialBalance;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal currentBalance;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal interestRate;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal minAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dueDate;
    private String type;

}
