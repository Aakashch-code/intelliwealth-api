package com.example.intelliwealth.treasury.goal.api;

import com.example.intelliwealth.treasury.goal.application.dto.GoalRequest;
import com.example.intelliwealth.treasury.goal.application.dto.GoalResponse;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStat;
import com.example.intelliwealth.treasury.goal.application.dto.StashRequest;
import com.example.intelliwealth.treasury.goal.application.service.GoalService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Goal Management", description = "APIs for managing financial goals, tracking progress, and statistics")
public class GoalController {

    private final GoalService service;

    @Operation(summary = "Get all goals")
    @GetMapping
    public Page<GoalResponse> getAllGoal(Pageable pageable) {

        return service.getAllGoal(pageable);
    }

    @Operation(summary = "Get goal by ID")
    @GetMapping("/{goalId}")
    public GoalResponse getGoalById(@PathVariable long goalId) {

        return service.getGoalById(goalId);
    }

    @Operation(summary = "Create goal")
    @PostMapping
    public GoalResponse createGoal(
            @Valid @RequestBody GoalRequest request) {

        return service.createGoal(request);
    }

    @Operation(summary = "Update goal")
    @PutMapping("/{goalId}")
    public GoalResponse updateGoalById(
            @PathVariable long goalId,
            @Valid @RequestBody GoalRequest request) {

        return service.updateGoal(goalId, request);
    }

    @Operation(summary = "Delete goal")
    @DeleteMapping("/{goalId}")
    public void deleteGoal(@PathVariable long goalId) {

        service.deleteGoalById(goalId);
    }

    @Hidden
    @DeleteMapping("/delete-all")
    public void deleteAllGoal() {

        service.deleteAllGoal();
    }

    @Operation(summary = "Get goal statistics")
    @GetMapping("/stats")
    public GoalStat getGoalStats() {

        return service.getGoalStats();
    }

    @Operation(summary = "Stash money into goal")
    @PutMapping("/{goalId}/stash")
    public GoalResponse stashMoney(
            @PathVariable Long goalId,
            @Valid @RequestBody StashRequest request) {

        return service.addFundsToGoal(goalId, request);
    }
}