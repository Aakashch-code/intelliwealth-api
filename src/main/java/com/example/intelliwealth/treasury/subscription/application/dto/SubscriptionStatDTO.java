package com.example.intelliwealth.treasury.subscription.application.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionStatDTO {

    private BigDecimal daily;
    private BigDecimal weekly;
    private BigDecimal monthly;
    private BigDecimal quarterly;
    private BigDecimal yearly;
}
