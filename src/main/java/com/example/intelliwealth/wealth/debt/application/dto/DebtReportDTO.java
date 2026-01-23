package com.example.intelliwealth.wealth.debt.application.dto;

import com.example.intelliwealth.wealth.debt.domain.model.DebtCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
public class DebtReportDTO {
    private BigDecimal totalDebt;
    private BigDecimal totalMonthlyEMI;
    private BigDecimal highestInterestRate;
    private List<DebtCategory> breakdown;
}
