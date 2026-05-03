package com.example.intelliwealth.wealth.asset.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CategorySumResult {
    private String id;
    private BigDecimal total;
}