package com.example.intelliwealth.treasury.goal.application.service;

import com.example.intelliwealth.authentication.application.SecuredService;
import com.example.intelliwealth.treasury.goal.application.dto.GoalRequestDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalResponseDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStatsResponseDTO;
import com.example.intelliwealth.treasury.goal.application.mapper.GoalMapper;
import com.example.intelliwealth.treasury.goal.domain.exception.GoalNotFoundException;
import com.example.intelliwealth.treasury.goal.domain.model.Goal;
import com.example.intelliwealth.treasury.goal.domain.model.GoalPeriod;
import com.example.intelliwealth.treasury.goal.infrastructure.persistence.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class GoalService extends SecuredService {

    private final GoalRepository repo;
    private final GoalMapper mapper;
    @Autowired
    private GoalCalculator goalCalculator;

    // ---------------- CRUD ----------------

    public List<GoalResponseDTO> getAllGoal() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public GoalResponseDTO getGoalById(long goalId) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        return mapper.toResponse(goal);
    }

    public GoalResponseDTO createGoal(GoalRequestDTO request) {
        Goal goal = mapper.toEntity(request);

        goal.setUserId(currentUserId());
        goal.setSpentAmount(BigDecimal.ZERO);

        if (goal.getCurrentAmount() == null) {
            goal.setCurrentAmount(BigDecimal.ZERO);
        }

        return mapper.toResponse(repo.save(goal));
    }

    public GoalResponseDTO updateGoal(long goalId, GoalRequestDTO request) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        // MapStruct handles the update logic automatically
        mapper.updateEntityFromRequest(goal, request);

        return mapper.toResponse(repo.save(goal));
    }

    public void deleteGoalById(long goalId) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        repo.delete(goal);
    }

    public void deleteAllGoal() {
        repo.deleteAllByUserId(currentUserId());
    }

    // ---------------- STATS ----------------

    public GoalStatsResponseDTO getGoalStats() {
        UUID userId = currentUserId();
        LocalDate today = LocalDate.now();

        List<Goal> goals = repo.findAllByUserId(userId);

        long totalGoals = goals.size();

        long completedGoals = goals.stream()
                .filter(this::isGoalCompleted)
                .count();

        BigDecimal totalTargetAmount =
                safeAmount(repo.sumTargetAmountByUserId(userId));

        BigDecimal totalCurrentAmount =
                safeAmount(repo.sumCurrentAmountByUserId(userId));

        long totalMonthlyRequired = goals.stream()
                .mapToLong(goal -> goalCalculator.calculateRequiredAmount(
                        goal.getTargetAmount(),
                        goal.getCurrentAmount(),
                        goal.getTargetDate(),
                        today,
                        GoalPeriod.MONTHLY
                ))
                .sum();

        return new GoalStatsResponseDTO(
                totalGoals,
                completedGoals,
                totalTargetAmount,
                totalCurrentAmount,
                totalMonthlyRequired
        );
    }

    private boolean isGoalCompleted(Goal goal) {
        return safeAmount(goal.getCurrentAmount())
                .compareTo(safeAmount(goal.getTargetAmount())) >= 0;
    }

    // Helper for stats calculation (BigDecimal null safety)
    private BigDecimal safeAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }
}