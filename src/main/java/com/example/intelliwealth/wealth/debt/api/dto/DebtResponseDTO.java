package com.example.intelliwealth.wealth.debt.api.dto;

import com.example.intelliwealth.wealth.debt.domain.model.DebtCategory;
import com.example.intelliwealth.wealth.debt.domain.model.DebtMainCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class DebtResponseDTO {

    private String id;
    private String name;
    private String creditor;

    private BigDecimal totalAmount;
    private BigDecimal outstandingAmount;

    private DebtCategory category;
    private DebtMainCategory mainCategory;

    private Map<String, Object> attributes;
    private Date dueDate;
}
