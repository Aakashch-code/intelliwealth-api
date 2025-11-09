package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.Goal;
import com.example.intelliwealth.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class GoalController {

    @Autowired
    private GoalService service;

    //Get
    @GetMapping("/goal")
    public List<Goal> getAllGoal() {
        return service.getAllGoal();
    }
    @GetMapping("/goal/{goalId}")
    public Goal getGoalById(@PathVariable int goalId) {
        return service.getGoalById(goalId);
    }

    //Put
    @PutMapping("/goal/{goalId}")
    public Goal updateGoalById (@RequestBody Goal goalId) {
        return service.updateGoal(goalId);
    }
    //Post
    @PostMapping("/goal")
    public Goal createGoal (@RequestBody Goal goal) {
        return service.createGoal(goal);
    }

    //Delete
    @DeleteMapping("/goal/{goalId}")
    public void deleteGoal (@PathVariable Goal goalId) {
        service.deleteGoalById(goalId);
    }
    @DeleteMapping("/goal")
    public void deleteAllGoal () {
        service.deleteAllGoal();
    }
}
