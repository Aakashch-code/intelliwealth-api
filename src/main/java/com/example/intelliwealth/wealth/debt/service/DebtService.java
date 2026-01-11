package com.example.intelliwealth.wealth.debt.service;

import com.example.intelliwealth.authentication.security.SecuredService;
import com.example.intelliwealth.wealth.debt.validation.DebtValidator;
import com.example.intelliwealth.wealth.debt.domain.Debt;
import com.example.intelliwealth.wealth.debt.dto.DebtRequestDTO;
import com.example.intelliwealth.wealth.debt.dto.DebtResponseDTO;
import com.example.intelliwealth.wealth.debt.exception.DebtNotFoundException;
import com.example.intelliwealth.wealth.debt.mapper.DebtMapper;
import com.example.intelliwealth.wealth.debt.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class DebtService extends SecuredService {

    private final DebtRepository repo;
    private final DebtMapper mapper;

    public List<DebtResponseDTO> getAll() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public DebtResponseDTO getById(String id) {
        Debt debt = repo.findById(id)
                .orElseThrow(() -> new DebtNotFoundException(id));

        if (!debt.getUserId().equals(currentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        return mapper.toDto(debt);
    }

    public DebtResponseDTO create(DebtRequestDTO dto) {

        if (dto.getAttributes() == null) {
            dto.setAttributes(new HashMap<>());
        }

        DebtValidator.validate(dto.getCategory(), dto.getAttributes());

        Debt debt = mapper.toEntity(dto);
        debt.setUserId(currentUserId());
        repo.findAllByUserId(currentUserId());


        return mapper.toDto(repo.save(debt));
    }

    public DebtResponseDTO update(String id, DebtRequestDTO dto) {

        Debt existing = repo.findById(id)
                .orElseThrow(() -> new DebtNotFoundException(id));

        if (!existing.getUserId().equals(currentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

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
        Debt debt = repo.findById(id)
                .orElseThrow(() -> new DebtNotFoundException(id));

        if (!debt.getUserId().equals(currentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        repo.delete(debt);
    }

    public BigDecimal totalDebt() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(d -> d.getOutstandingAmount() == null
                        ? BigDecimal.ZERO
                        : d.getOutstandingAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalMonthlyEMIs() {
        List<Debt> debts = repo.findAllByUserId(currentUserId());

        BigDecimal total = BigDecimal.ZERO;

        for (Debt debt : debts) {
            Map<String, Object> attrs = debt.getAttributes();
            if (attrs == null) continue;

            BigDecimal monthly = switch (debt.getCategory()) {
                case HOME_LOAN, PERSONAL_LOAN, CAR_LOAN ->
                        extractAmount(attrs, "emi");
                case EMI ->
                        extractAmount(attrs, "emiAmount");
                case CREDIT_CARD ->
                        extractAmount(attrs, "minPayment");
                case FRIEND_LOAN ->
                        extractAmount(attrs, "repaymentAmount");
            };

            total = total.add(monthly);
        }
        return total;
    }

    private BigDecimal extractAmount(Map<String, Object> attributes, String key) {
        Object val = attributes.get(key);
        if (val == null) return BigDecimal.ZERO;

        try {
            return new BigDecimal(val.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
