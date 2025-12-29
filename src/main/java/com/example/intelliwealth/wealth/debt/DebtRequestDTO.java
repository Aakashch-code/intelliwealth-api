package com.example.intelliwealth.wealth.debt;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
@Data
public class DebtRequestDTO {

    private String name;
    private String creditor;

    private BigDecimal totalAmount;
    private BigDecimal outstandingAmount;

    private DebtCategory category;
    private DebtMainCategory mainCategory;

    private Map<String, Object> attributes;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date dueDate;
}
