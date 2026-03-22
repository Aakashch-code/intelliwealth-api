package com.example.intelliwealth.treasury.goal.api;

import com.example.intelliwealth.treasury.goal.application.dto.GoalRequest;
import com.example.intelliwealth.treasury.goal.application.dto.GoalResponse;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStat;
import com.example.intelliwealth.treasury.goal.application.dto.StashRequest;
import com.example.intelliwealth.treasury.goal.application.service.GoalService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Goal Management", description = "APIs for managing financial goals, tracking progress, and statistics")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService service;

    @Order(1)
    @Operation(summary = "Get all goals")
    @GetMapping("/goal")
    public Page<GoalResponse> getAllGoal(Pageable pageable) {
        return service.getAllGoal(pageable);
    }

    @Operation(summary = "Get goal by ID")
    @GetMapping("/goal/{goalId}")
    public GoalResponse getGoalById(@Parameter(description = "ID of the goal") @PathVariable long goalId) {
        return service.getGoalById(goalId);
    }

    @Operation(summary = "Create a new goal")
    @PostMapping("/goal")
    public GoalResponse createGoal(@RequestBody GoalRequest request) {
        return service.createGoal(request);
    }

    @Operation(summary = "Update a goal")
    @PutMapping("/goal/{goalId}")
    public GoalResponse updateGoalById(
            @Parameter(description = "ID of the goal") @PathVariable long goalId,
            @RequestBody GoalRequest request) {
        return service.updateGoal(goalId, request);
    }

    @Operation(summary = "Delete a goal")
    @DeleteMapping("/goal/{goalId}")
    public void deleteGoal(@Parameter(description = "ID of the goal") @PathVariable long goalId) {
        service.deleteGoalById(goalId);
    }

    @Hidden
    @Operation(summary = "Delete ALL goals")
    @DeleteMapping("/goal/delete-all")
    public void deleteAllGoal() {
        service.deleteAllGoal();
    }

    @Operation(summary = "Get goal statistics")
    @GetMapping("/goal/stats")
    public GoalStat getGoalStats() {
        return service.getGoalStats();
    }

    @Operation(
            summary = "Stash money into a goal",
            description = "Allocates funds towards a goal without marking it as an expense."
    )
    @PutMapping("/goals/{goalId}/stash")
    public GoalResponse stashMoney(@PathVariable Long goalId, @Valid @RequestBody StashRequest request) {
        return service.addFundsToGoal(goalId, request);
    }
}