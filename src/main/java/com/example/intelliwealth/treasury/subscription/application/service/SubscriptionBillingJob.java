package com.example.intelliwealth.treasury.subscription.application.service;

import com.example.intelliwealth.treasury.subscription.domain.model.BillingCycle;
import com.example.intelliwealth.treasury.subscription.domain.model.Subscription;
import com.example.intelliwealth.treasury.subscription.infrastrcture.persistence.SubscriptionRepository;
import com.example.intelliwealth.treasury.transaction.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionBillingJob {

    private final SubscriptionRepository subscriptionRepository;
    private final TransactionService transactionService;

    // This cron expression runs the job every day at midnight (00:00) server time
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void processRecurringSubscriptions() {
        LocalDate today = LocalDate.now();

        // 1. Find all ACTIVE subscriptions where the nextBillingDate is today (or earlier, just in case the server was down)
        // Note: You will need to add this method to your SubscriptionRepository!
        List<Subscription> dueSubscriptions = subscriptionRepository.findByIsActiveTrueAndNextBillingDateLessThanEqual(today);

        for (Subscription sub : dueSubscriptions) {
            // 2. Log the recurring expense
            transactionService.createSystemExpense(
                    null, // Again, add budgetId if applicable
                    sub.getId(),
                    sub.getAmount(),
                    sub.getTitle(),
                    "Recurring subscription payment"
            );

            // 3. Update the subscription with the NEXT billing date
            LocalDate nextDate = calculateNextBillingDate(today, sub.getBillingCycle());
            sub.setNextBillingDate(nextDate);
        }

        // 4. Save all updated subscriptions back to the database
        subscriptionRepository.saveAll(dueSubscriptions);
    }

    private LocalDate calculateNextBillingDate(LocalDate currentDate, BillingCycle cycle) {
        return switch (cycle) {
            case WEEKLY -> currentDate.plusWeeks(1);
            case MONTHLY -> currentDate.plusMonths(1);
            case QUARTERLY -> currentDate.plusMonths(3);
            case ANNUAL -> currentDate.plusYears(1);
            default -> currentDate.plusMonths(1);
        };
    }
}