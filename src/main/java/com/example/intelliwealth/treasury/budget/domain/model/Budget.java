package com.example.intelliwealth.treasury.budget.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private BudgetCategory category;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal amountAllocated;
    private BigDecimal amountSpent;

    private boolean recurring;
    private String note;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ===== Derived Fields =====

    @Transient
    public BigDecimal getRemainingAmount() {
        return amountAllocated.subtract(
                amountSpent != null ? amountSpent : BigDecimal.ZERO
        );
    }

    @Transient
    public BudgetStatus getStatus() {
        if (amountSpent == null || amountAllocated == null) return BudgetStatus.SAFE;

        BigDecimal usage =
                amountSpent.divide(amountAllocated, 2, RoundingMode.HALF_UP);

        if (usage.compareTo(BigDecimal.ONE) >= 0) return BudgetStatus.EXCEEDED;
        if (usage.compareTo(new BigDecimal("0.8")) >= 0) return BudgetStatus.WARNING;
        return BudgetStatus.SAFE;
    }
}
