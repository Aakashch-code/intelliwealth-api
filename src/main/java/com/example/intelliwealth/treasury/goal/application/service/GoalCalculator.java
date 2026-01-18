package com.example.intelliwealth.treasury.goal.application.service;

import com.example.intelliwealth.treasury.goal.domain.model.GoalPeriod;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
@Component
public class GoalCalculator {

    public long calculateRequiredAmount(
            BigDecimal targetAmount,
            BigDecimal currentAmount,
            LocalDate targetDate,
            LocalDate fromDate,
            GoalPeriod period
    ) {
        if (targetAmount == null || currentAmount == null || targetDate == null) {
            return 0;
        }

        if (!targetDate.isAfter(fromDate)) {
            return 0;
        }

        BigDecimal remaining = targetAmount.subtract(currentAmount);
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        long units = switch (period) {
            case WEEKLY -> ChronoUnit.WEEKS.between(fromDate, targetDate);
            case MONTHLY -> ChronoUnit.MONTHS.between(
                    fromDate.withDayOfMonth(1),
                    targetDate.withDayOfMonth(1)
            );
            case DAILY -> ChronoUnit.DAYS.between(fromDate, targetDate);
        };

        if (units <= 0) {
            return 0;
        }

        return remaining
                .divide(BigDecimal.valueOf(units), 0, RoundingMode.UP)
                .longValue();
    }

}

