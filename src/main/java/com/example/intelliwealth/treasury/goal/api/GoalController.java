package com.example.intelliwealth.treasury.goal.api;

import com.example.intelliwealth.treasury.goal.application.dto.GoalRequestDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalResponseDTO;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStatsResponseDTO;
import com.example.intelliwealth.treasury.goal.application.service.GoalService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@Tag(name = "Goal Management", description = "APIs for managing financial goals, tracking progress, and statistics")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService service;

    @Operation(summary = "Get all goals")
    @GetMapping("/goal")
    public List<GoalResponseDTO> getAllGoal() {
        return service.getAllGoal();
    }

    @Operation(summary = "Get goal by ID")
    @GetMapping("/goal/{goalId}")
    public GoalResponseDTO getGoalById(@Parameter(description = "ID of the goal") @PathVariable long goalId) {
        return service.getGoalById(goalId);
    }

    @Operation(summary = "Create a new goal")
    @PostMapping("/goal")
    public GoalResponseDTO createGoal(@RequestBody GoalRequestDTO request) {
        return service.createGoal(request);
    }

    @Operation(summary = "Update a goal")
    @PutMapping("/goal/{goalId}")
    public GoalResponseDTO updateGoalById(
            @Parameter(description = "ID of the goal") @PathVariable long goalId,
            @RequestBody GoalRequestDTO request) {
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
    public GoalStatsResponseDTO getGoalStats() {
        return service.getGoalStats();
    }
}