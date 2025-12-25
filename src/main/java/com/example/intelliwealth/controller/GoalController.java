package com.example.intelliwealth.controller;

import com.example.intelliwealth.dto.goal.AddFundsRequest;
import com.example.intelliwealth.dto.goal.GoalRequest;
import com.example.intelliwealth.dto.goal.GoalResponse;
import com.example.intelliwealth.dto.goal.GoalStatsResponse;
import com.example.intelliwealth.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
@Tag(name = "Goal Management", description = "APIs for managing financial goals, tracking progress, and statistics")
public class GoalController {

    @Autowired
    private GoalService service;

    @Operation(summary = "Get all goals", description = "Retrieve a list of all financial goals")
    @GetMapping("/goal")
    public List<GoalResponse> getAllGoal() {
        return service.getAllGoal();
    }

    @Operation(summary = "Get goal by ID", description = "Retrieve a specific goal by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goal found"),
            @ApiResponse(responseCode = "404", description = "Goal not found")
    })
    @GetMapping("/goal/{goalId}")
    public GoalResponse getGoalById(@Parameter(description = "ID of the goal to retrieve") @PathVariable long goalId) {
        return service.getGoalById(goalId);
    }

    @Operation(summary = "Create a new goal", description = "Create a new financial goal with target amount and date")
    @PostMapping("/goal")
    public GoalResponse createGoal(@RequestBody GoalRequest request) {
        return service.createGoal(request);
    }

    @Operation(summary = "Update a goal", description = "Update details of an existing goal (name, target, priority, etc)")
    @PutMapping("/goal/{goalId}")
    public GoalResponse updateGoalById(
            @Parameter(description = "ID of the goal to update") @PathVariable long goalId,
            @RequestBody GoalRequest request) {
        return service.updateGoal(goalId, request);
    }

    @Operation(summary = "Delete a goal", description = "Permanently remove a goal by its ID")
    @DeleteMapping("/goal/{goalId}")
    public void deleteGoal(@Parameter(description = "ID of the goal to delete") @PathVariable long goalId) {
        service.deleteGoalById(goalId);
    }

    @Operation(summary = "Delete ALL goals", description = "WARNING: Permanently removes all goals from the database")
    @DeleteMapping("/goal/delete-all")
    public void deleteAllGoal() {
        service.deleteAllGoal();
    }

    @Operation(summary = "Get goal statistics", description = "Get aggregated stats like total saved, total target, and completion counts")
    @GetMapping("/goal/stats")
    public GoalStatsResponse getGoalStats() {
        return service.getGoalStats();
    }

    @Operation(summary = "Add funds to goal", description = "Increment the current amount of a specific goal")
    @PutMapping("/goal/{id}/add-funds")
    public GoalResponse addGoalFunds(
            @Parameter(description = "ID of the goal to add funds to") @PathVariable long id,
            @RequestBody AddFundsRequest request) {
        return service.addFunds(id, request.getAmount());
    }
}