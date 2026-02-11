package com.example.intelliwealth.treasury.subscription.application.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionStatDTO {

    private BigDecimal daily;
    private BigDecimal weekly;
    private BigDecimal monthly;
    private BigDecimal quarterly;
    private BigDecimal yearly;
}
