package com.example.intelliwealth.wealth.debt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
}
