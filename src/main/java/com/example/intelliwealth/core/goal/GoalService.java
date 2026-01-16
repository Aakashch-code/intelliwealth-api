package com.example.intelliwealth.core.goal;

import com.example.intelliwealth.authentication.application.SecuredService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class GoalService extends SecuredService {

    private final GoalRepository repo;

    // ---------------- CRUD ----------------

    public List<GoalResponseDTO> getAllGoal() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public GoalResponseDTO getGoalById(long goalId) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        return mapToResponse(goal);
    }

    public GoalResponseDTO createGoal(GoalRequestDTO request) {
        Goal goal = new Goal();
        goal.setUserId(currentUserId()); // 🔐 owner

        goal.setCurrentAmount(
                request.getCurrentAmount() != null
                        ? request.getCurrentAmount()
                        : BigDecimal.ZERO
        );
        goal.setSpentAmount(BigDecimal.ZERO);

        updateEntityFromRequest(goal, request);

        return mapToResponse(repo.save(goal));
    }

    public GoalResponseDTO updateGoal(long goalId, GoalRequestDTO request) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        updateEntityFromRequest(goal, request);

        if (request.getCurrentAmount() != null) {
            goal.setCurrentAmount(request.getCurrentAmount());
        }

        return mapToResponse(repo.save(goal));
    }

    public void deleteGoalById(long goalId) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        repo.delete(goal);
    }

    public void deleteAllGoal() {
        repo.deleteAllByUserId(currentUserId());
    }

    // ---------------- PROGRESS ----------------

    public GoalResponseDTO addFunds(long goalId, BigDecimal amount) {
        Goal goal = repo.findByIdAndUserId(goalId, currentUserId())
                .orElseThrow(() -> new com.example.intelliwealth.core.goal.GoalNotFoundException("Goal not found"));

        goal.setCurrentAmount(
                safeAmount(goal.getCurrentAmount()).add(amount)
        );

        return mapToResponse(repo.save(goal));
    }

    // ---------------- STATS ----------------

    public GoalStatsResponseDTO getGoalStats() {
        List<Goal> goals = repo.findAllByUserId(currentUserId());

        long totalGoals = goals.size();

        long completedGoals = goals.stream()
                .filter(g ->
                        safeAmount(g.getCurrentAmount())
                                .compareTo(safeAmount(g.getTargetAmount())) >= 0
                )
                .count();

        BigDecimal totalTargetAmount = goals.stream()
                .map(g -> safeAmount(g.getTargetAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCurrentAmount = goals.stream()
                .map(g -> safeAmount(g.getCurrentAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new GoalStatsResponseDTO(
                totalGoals,
                completedGoals,
                totalTargetAmount,
                totalCurrentAmount
        );
    }

    // ---------------- HELPERS ----------------

    private GoalResponseDTO mapToResponse(Goal goal) {
        GoalResponseDTO response = new GoalResponseDTO();
        response.setId(goal.getId());
        response.setName(goal.getName());
        response.setTargetAmount(goal.getTargetAmount());
        response.setCurrentAmount(goal.getCurrentAmount());
        response.setSpentAmount(goal.getSpentAmount());
        response.setTargetDate(goal.getTargetDate());
        response.setPriority(goal.getPriority());
        return response;
    }

    private void updateEntityFromRequest(Goal goal, GoalRequestDTO request) {
        goal.setName(request.getName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setTargetDate(request.getTargetDate());
        goal.setPriority(request.getPriority());
    }

    private BigDecimal safeAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }
}
