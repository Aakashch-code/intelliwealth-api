package com.example.intelliwealth.treasury.budget.domain.model;

import com.mongodb.lang.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "budget")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetCategory category;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal amountAllocated;

    @Column(nullable = false)
    private BigDecimal amountSpent;

    private boolean recurring;

    private String note;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public BigDecimal getRemainingAmount() {
        BigDecimal allocated = amountAllocated != null ? amountAllocated : BigDecimal.ZERO;
        BigDecimal spent = amountSpent != null ? amountSpent : BigDecimal.ZERO;
        return allocated.subtract(spent);
    }

    public BudgetStatus getStatus() {
        if (amountSpent == null || amountAllocated == null || amountAllocated.compareTo(BigDecimal.ZERO) == 0) {
            return BudgetStatus.SAFE;
        }

        BigDecimal usage = amountSpent.divide(amountAllocated, 2, RoundingMode.HALF_UP);

        if (usage.compareTo(BigDecimal.ONE) >= 0) return BudgetStatus.EXCEEDED;
        if (usage.compareTo(new BigDecimal("0.8")) >= 0) return BudgetStatus.WARNING;
        return BudgetStatus.SAFE;
    }
}