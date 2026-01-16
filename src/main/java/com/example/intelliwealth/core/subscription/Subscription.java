package com.example.intelliwealth.core.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Hidden
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private UUID userId;
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String billingCycle;

    @Column(nullable = false)
    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate nextRecurrence;

    @Column(name = "is_active")
    private boolean isActive = true;
}
