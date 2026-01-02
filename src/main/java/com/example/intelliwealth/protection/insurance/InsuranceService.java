package com.example.intelliwealth.protection.insurance;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class InsuranceService {

    private final InsuranceRepository repo;
    private final InsuranceMapper mapper;

    public InsuranceService(InsuranceRepository repo, InsuranceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    // --- Create ---
    public InsuranceResponseDTO create(InsuranceRequestDTO dto) {
        InsuranceValidator.validateAttributes(dto.getCategory(), dto.getAttributes());
        Insurance insurance = mapper.toEntity(dto);
        return mapper.toDTO(repo.save(insurance));
    }

    // --- Read ---
    public List<InsuranceResponseDTO> getAll() {
        return repo.findAll().stream().map(mapper::toDTO).toList();
    }

    public InsuranceResponseDTO getById(String id) {
        return repo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Insurance policy not found with ID: " + id));
    }

    public List<InsuranceResponseDTO> getByCategory(InsuranceCategory category) {
        return repo.findByCategory(category).stream().map(mapper::toDTO).toList();
    }

    public List<InsuranceResponseDTO> getActivePolicies() {
        return repo.findActivePolicies(LocalDate.now()).stream().map(mapper::toDTO).toList();
    }

    public List<InsuranceResponseDTO> getExpiringSoon() {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        return repo.findByEndDateBetween(today, nextMonth).stream().map(mapper::toDTO).toList();
    }

    // --- Update ---
    @Transactional
    public InsuranceResponseDTO update(String id, InsuranceRequestDTO dto) {
        Insurance existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Insurance policy not found with ID: " + id));

        // Validate potential new attributes
        InsuranceValidator.validateAttributes(dto.getCategory(), dto.getAttributes());

        // Update fields (Mapper logic could also handle this "partial update" if configured)
        existing.setName(dto.getName());
        existing.setProvider(dto.getProvider());
        existing.setCategory(dto.getCategory());
        existing.setMainCategory(dto.getMainCategory());
        existing.setPremiumAmount(dto.getPremiumAmount());
        existing.setCoverageAmount(dto.getCoverageAmount());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setAttributes(dto.getAttributes());

        return mapper.toDTO(repo.save(existing));
    }

    // --- Delete ---
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Insurance policy not found with ID: " + id);
        }
        repo.deleteById(id);
    }
}