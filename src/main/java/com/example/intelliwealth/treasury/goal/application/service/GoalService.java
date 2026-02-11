package com.example.intelliwealth.treasury.goal.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.treasury.goal.application.dto.GoalMinimalDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalRequestDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalResponseDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStatDTO;
import com.example.intelliwealth.treasury.goal.domain.exception.GoalNotFoundException;
import com.example.intelliwealth.treasury.goal.domain.model.Goal;
import com.example.intelliwealth.treasury.goal.domain.model.GoalPeriod;
import com.example.intelliwealth.treasury.goal.infrastructure.mapper.GoalMapper;
import com.example.intelliwealth.treasury.goal.infrastructure.persistence.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class GoalService extends SecuredService {

    private final GoalRepository repo;
    private final GoalMapper mapper;
    private final GoalCalculator goalCalculator;

    // ---------------- CRUD OPERATIONS ----------------

    @Transactional(readOnly = true)
    public Page<GoalResponseDTO> getAllGoal(Pageable pageable) {
        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException("Page size is too high");
        }
        return repo.findAllByUserId(currentUserId(), pageable)
                .map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public GoalResponseDTO getGoalById(long goalId) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));
        return mapper.toResponse(goal);
    }

    @CacheEvict(value = "goal_stat", key = "#root.target.cacheKey()")
    public GoalResponseDTO createGoal(GoalRequestDTO request) {
        Goal goal = mapper.toEntity(request);
        goal.setUserId(currentUserId());
        goal.setSpentAmount(BigDecimal.ZERO);

        if (goal.getCurrentAmount() == null) {
            goal.setCurrentAmount(BigDecimal.ZERO);
        }

        return mapper.toResponse(repo.save(goal));
    }

    @CacheEvict(value = "goal_stat", key = "#root.target.cacheKey()")
    public GoalResponseDTO updateGoal(long goalId, GoalRequestDTO request) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        mapper.updateEntityFromRequest(goal, request);
        return mapper.toResponse(repo.save(goal));
    }

    @CacheEvict(value = "goal_stat", key = "#root.target.cacheKey()")
    public void deleteGoalById(long goalId) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));
        repo.delete(goal);
    }

    @CacheEvict(value = "goal_stat", key = "#root.target.cacheKey()")
    public void deleteAllGoal() {
        repo.deleteAllByUserId(currentUserId());
    }

    // ---------------- AGGREGATION / STATS ----------------

    @Transactional(readOnly = true)
    @Cacheable(value = "goal_stat", key = "#root.target.cacheKey()")
    public GoalStatDTO getGoalStats() {
        GoalStatDTO stats = repo.getStats(currentUserId());
        List<GoalMinimalDTO> goals = repo.findMinimalGoal(currentUserId());

        long totalMonthlyRequired = goals.stream()
                .mapToLong(goal -> goalCalculator.calculateRequiredAmount(
                        goal.getTargetAmount(),
                        goal.getCurrentAmount(),
                        goal.getTargetDate(),
                        LocalDate.now(),
                        GoalPeriod.MONTHLY
                ))
                .sum();

        return new GoalStatDTO(
                stats.getTotalGoals(),
                stats.getCompletedGoals(),
                stats.getTotalTarget(),
                stats.getTotalCurrent(),
                totalMonthlyRequired
        );
    }
}