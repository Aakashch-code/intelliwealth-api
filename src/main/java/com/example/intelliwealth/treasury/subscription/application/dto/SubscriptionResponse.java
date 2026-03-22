package com.example.intelliwealth.treasury.subscription.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SubscriptionResponse {

    private int id;
    private String title;
    private BigDecimal amount;
    private String billingCycle;
    private String category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nextRecurrence;
    private boolean isActive = true;

}
