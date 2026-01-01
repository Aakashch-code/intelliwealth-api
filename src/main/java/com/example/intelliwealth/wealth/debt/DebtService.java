package com.example.intelliwealth.wealth.debt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository repo;
    private final DebtMapper mapper;

    public List<DebtResponseDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public DebtResponseDTO getById(String id) {
        return mapper.toDto(
                repo.findById(id)
                        .orElseThrow(() -> new DebtNotFoundException(id))
        );
    }

    public DebtResponseDTO create(DebtRequestDTO dto) {

        if (dto.getAttributes() == null)
            dto.setAttributes(new HashMap<>());

        DebtValidator.validate(dto.getCategory(), dto.getAttributes());

        Debt debt = mapper.toEntity(dto);
        return mapper.toDto(repo.save(debt));
    }

    public DebtResponseDTO update(String id, DebtRequestDTO dto) {

        Debt existing = repo.findById(id)
                .orElseThrow(() -> new DebtNotFoundException(id));

        DebtValidator.validate(dto.getCategory(), dto.getAttributes());

        existing.setName(dto.getName());
        existing.setCreditor(dto.getCreditor());
        existing.setTotalAmount(dto.getTotalAmount());
        existing.setOutstandingAmount(dto.getOutstandingAmount());
        existing.setCategory(dto.getCategory());
        existing.setMainCategory(dto.getMainCategory());
        existing.setAttributes(dto.getAttributes());
        existing.setDueDate(dto.getDueDate());

        return mapper.toDto(repo.save(existing));
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public BigDecimal totalDebt() {
        return repo.findAll()
                .stream()
                .map(d -> d.getOutstandingAmount() == null
                        ? BigDecimal.ZERO
                        : d.getOutstandingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal getTotalMonthlyEMIs() {
        List<Debt> allDebts = repo.findAll(); // Or findByStatus("ACTIVE") if you have that
        BigDecimal totalObligation = BigDecimal.ZERO;

        for (Debt debt : allDebts) {
            // Assuming Debt entity has a Map<String, Object> or similar for attributes
            // If strictly mapped, adjust getters accordingly.
            Map<String, Object> attributes = debt.getAttributes();

            if (attributes == null || attributes.isEmpty()) continue;

            BigDecimal monthlyPay = BigDecimal.ZERO;

            switch (debt.getCategory()) {
                case HOME_LOAN, PERSONAL_LOAN, CAR_LOAN -> {
                    // Try to find 'emi'
                    monthlyPay = extractAmount(attributes, "emi");
                }
                case EMI -> {
                    // Rules say 'emiAmount'
                    monthlyPay = extractAmount(attributes, "emiAmount");
                }
                case CREDIT_CARD -> {
                    // Rules say 'minPayment'
                    monthlyPay = extractAmount(attributes, "minPayment");
                }
                case FRIEND_LOAN -> {
                    // Fallback to generic amount or 0 if not defined
                    monthlyPay = extractAmount(attributes, "repaymentAmount");
                }
            }
            totalObligation = totalObligation.add(monthlyPay);
        }
        return totalObligation;
    }

    // Helper to safely extract BigDecimal from Map (handles String/Double/Int types)
    private BigDecimal extractAmount(Map<String, Object> attributes, String key) {
        if (!attributes.containsKey(key)) return BigDecimal.ZERO;

        Object val = attributes.get(key);
        try {
            return new BigDecimal(val.toString());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

}
