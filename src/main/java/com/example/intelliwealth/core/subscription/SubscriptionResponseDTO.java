package com.example.intelliwealth.core.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SubscriptionResponseDTO {

    private int id;
    private String title; //Spotify
    private BigDecimal amount; //199
    private String billingCycle; //Monthly
    private String category; //Entertainment
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nextRecurrence; //Next Recurring Date
    private boolean isActive = true; // always true

}
