package com.example.intelliwealth.treasury.budget.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequestDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetSummaryDTO;
import com.example.intelliwealth.treasury.budget.infrastructure.mapper.BudgetMapper;
import com.example.intelliwealth.treasury.budget.domain.exception.BudgetNotFoundException;
import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import com.example.intelliwealth.treasury.budget.infrastructure.persistence.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class BudgetService extends SecuredService{

    private final BudgetRepository repo;
    private final BudgetMapper mapper;



    @CacheEvict(value = "budget_summary" , key = "#root.target.cacheKey()")
    public BudgetResponseDTO createBudget(BudgetRequestDTO request) {
        Budget budget = mapper.toEntity(request);
        budget.setUserId(currentUserId());
        return mapper.toResponseDTO(repo.save(budget));
    }

    @Transactional(readOnly = true)
    public Page<BudgetResponseDTO> getBudgets(Pageable pageable) {
        if(pageable.getPageSize()>100) {
            throw new IllegalArgumentException("Page  size is too large");
        }
        return repo.findAllByUserId(currentUserId(), pageable)
                .map(mapper::toResponseDTO);
    }


    @Transactional(readOnly = true)
    public BudgetResponseDTO getBudgetById(Long id) {
        return repo.findByIdAndUserId(id, currentUserId())
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + id + " not found"));
    }

    @CacheEvict(value = "budget_summary" , key = "#root.target.cacheKey()")
    public BudgetResponseDTO updateBudget(Long id, BudgetRequestDTO request) {
        Budget existing = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + id + " not found"));

        mapper.updateEntityFromRequest(existing, request);
        return mapper.toResponseDTO(repo.save(existing));
    }

    @CacheEvict(value = "budget_summary" , key = "#root.target.cacheKey()")
    public void deleteBudgetById(Long id) {
        Budget budget = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID " + id + " not found"));

        repo.delete(budget);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "budget_summary" , key = "#root.target.cacheKey()" , unless = "#result==null")
    public BudgetSummaryDTO getBudgetSummary() {
        return repo.findBudgetSummaryByUserId(currentUserId());
    }

    public void deleteAllBudget() {
        repo.deleteAll();
    }
}