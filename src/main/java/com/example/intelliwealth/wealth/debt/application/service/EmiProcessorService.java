package com.example.intelliwealth.wealth.debt.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatus;
import com.example.intelliwealth.wealth.debt.domain.model.Debt;

import com.example.intelliwealth.wealth.debt.infrastructure.persistence.DebtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmiProcessorService extends SecuredService {

    private final DebtRepository debtRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void processDailyEMIs() {
        log.info("Starting daily EMI processing...");

        LocalDate today = LocalDate.now();
        List<Debt> activeDebts = debtRepository.findAllByUserIdAndStatus(currentUserId(), DebtStatus.ACTIVE);

        for (Debt debt : activeDebts) {
            if (debt.getDueDate() != null && !today.isBefore(debt.getDueDate())) {

                Map<String, Object> attrs = debt.getAttributes();

                if (attrs != null && attrs.containsKey("emiAmount") && attrs.containsKey("remainingTenureMonths")) {
                    try {
                        BigDecimal emiAmount = new BigDecimal(attrs.get("emiAmount").toString());
                        int remainingTenure = Integer.parseInt(attrs.get("remainingTenureMonths").toString());

                        BigDecimal newOutstanding = debt.getOutstandingAmount().subtract(emiAmount);
                        if (newOutstanding.compareTo(BigDecimal.ZERO) < 0) {
                            newOutstanding = BigDecimal.ZERO;
                        }
                        debt.setOutstandingAmount(newOutstanding);

                        remainingTenure = remainingTenure - 1;
                        attrs.put("remainingTenureMonths", remainingTenure);
                        debt.setAttributes(attrs);

                        debt.setDueDate(debt.getDueDate().plusMonths(1));

                        if (newOutstanding.compareTo(BigDecimal.ZERO) == 0 || remainingTenure <= 0) {
                            debt.setStatus(DebtStatus.PAID);
                            log.info("Debt {} is fully paid. Marking as PAID.", debt.getId());
                        }

                        debtRepository.save(debt);
                        log.info("Processed EMI for debt ID: {}. Next due date: {}", debt.getId(), debt.getDueDate());

                    } catch (Exception e) {
                        log.error("Failed to process EMI for debt ID: {}", debt.getId(), e);
                    }
                }
            }
        }

        log.info("Finished daily EMI processing.");
    }
}