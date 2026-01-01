package com.example.intelliwealth.core.goal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository repo;

    public GoalService(GoalRepository repo) {
        this.repo = repo;
    }

    // -------------------------
    // CRUD Operations
    // -------------------------

    public List<GoalResponseDTO> getAllGoal() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public GoalResponseDTO getGoalById(long goalId) {
        Goal goal = repo.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));
        return mapToResponse(goal);
    }

    public GoalResponseDTO createGoal(GoalRequestDTO request) {
        Goal goal = new Goal();
        goal.setCurrentAmount(request.getCurrentAmount() != null ? request.getCurrentAmount() : BigDecimal.ZERO);
        goal.setSpentAmount(BigDecimal.ZERO); // Initialize spent amount

        updateEntityFromRequest(goal, request); // Map fields

        Goal savedGoal = repo.save(goal);
        return mapToResponse(savedGoal);
    }

    public GoalResponseDTO updateGoal(long goalId, GoalRequestDTO request) {
        Goal goal = repo.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        updateEntityFromRequest(goal, request); // Update fields

        // If current amount is passed in update, update it, otherwise keep existing
        if(request.getCurrentAmount() != null) {
            goal.setCurrentAmount(request.getCurrentAmount());
        }

        Goal savedGoal = repo.save(goal);
        return mapToResponse(savedGoal);
    }

    public void deleteGoalById(long goalId) {
        if (!repo.existsById(goalId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found");
        }
        repo.deleteById(goalId);
    }

    public void deleteAllGoal() {
        repo.deleteAll();
    }

    // -------------------------
    // Goal Progress Operations
    // -------------------------

    public GoalResponseDTO addFunds(long goalId, BigDecimal amount) {
        Goal goal = repo.findById(goalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found"));

        BigDecimal current = goal.getCurrentAmount() == null ? BigDecimal.ZERO : goal.getCurrentAmount();
        goal.setCurrentAmount(current.add(amount));

        Goal savedGoal = repo.save(goal);
        return mapToResponse(savedGoal);
    }

    // -------------------------
    // Statistics (Unchanged mostly)
    // -------------------------
    public GoalStatsResponseDTO getGoalStats() {
        List<Goal> goals = repo.findAll();

        long totalGoals = goals.size();

        long completedGoals = goals.stream()
                .filter(g -> safeAmount(g.getCurrentAmount())
                        .compareTo(safeAmount(g.getTargetAmount())) >= 0)
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

    // -------------------------
    // Helper Methods (Manual Mapping)
    // -------------------------

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