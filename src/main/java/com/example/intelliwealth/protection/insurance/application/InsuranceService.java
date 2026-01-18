package com.example.intelliwealth.protection.insurance.application;

import com.example.intelliwealth.authentication.domain.Users;
import com.example.intelliwealth.protection.insurance.domain.model.Insurance;
import com.example.intelliwealth.protection.insurance.domain.model.InsuranceCategory;
import com.example.intelliwealth.protection.insurance.api.dto.InsuranceRequestDTO;
import com.example.intelliwealth.protection.insurance.api.dto.InsuranceResponseDTO;
import com.example.intelliwealth.protection.insurance.application.mapper.InsuranceMapper;
import com.example.intelliwealth.protection.insurance.infrastructure.persistence.InsuranceRepository;
import com.example.intelliwealth.protection.insurance.domain.rules.InsuranceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository repo;
    private final InsuranceMapper mapper;

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated");
        }

        if (auth.getPrincipal() instanceof Users user) {
            return user.getId();
        }

        throw new AccessDeniedException("Invalid authentication principal");
    }

    public InsuranceResponseDTO create(InsuranceRequestDTO dto) {
        InsuranceValidator.validateAttributes(dto.getCategory(), dto.getAttributes());

        Insurance insurance = mapper.toEntity(dto);
        insurance.setUserId(getCurrentUserId());

        return mapper.toDTO(repo.save(insurance));
    }

    public List<InsuranceResponseDTO> getAll() {
        return repo.findAllByUserId(getCurrentUserId())
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public InsuranceResponseDTO getById(String id) {
        Insurance insurance = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Policy not found"));

        if (!insurance.getUserId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        return mapper.toDTO(insurance);
    }

    public List<InsuranceResponseDTO> getByCategory(InsuranceCategory category) {
        return repo.findByUserIdAndCategory(getCurrentUserId(), category)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<InsuranceResponseDTO> getActivePolicies() {
        return repo.findActivePoliciesForUser(getCurrentUserId(), LocalDate.now())
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<InsuranceResponseDTO> getExpiringSoon() {
        LocalDate now = LocalDate.now();
        return repo.findByUserIdAndEndDateBetween(
                        getCurrentUserId(),
                        now,
                        now.plusMonths(1)
                )
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional
    public InsuranceResponseDTO update(String id, InsuranceRequestDTO dto) {
        Insurance existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Policy not found"));

        if (!existing.getUserId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        InsuranceValidator.validateAttributes(dto.getCategory(), dto.getAttributes());

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

    public void delete(String id) {
        Insurance insurance = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Policy not found"));

        if (!insurance.getUserId().equals(getCurrentUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        repo.delete(insurance);
    }
}
