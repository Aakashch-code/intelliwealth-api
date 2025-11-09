package com.example.intelliwealth.service;

import com.example.intelliwealth.model.Goal;
import com.example.intelliwealth.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {

    @Autowired
    private GoalRepository repo;

    public List<Goal> getAllGoal() {
       return repo.findAll();
    }

    public Goal getGoalById(long goalId) {
        return repo.findById(goalId).orElse(new Goal());
    }

    public Goal updateGoal(Goal goalId) {
        return repo.save(goalId);
    }

    public Goal createGoal(Goal goal) {
        return repo.save(goal);
    }

    public void deleteGoalById(Goal goalId) {
        repo.delete(goalId);
    }

    public void deleteAllGoal() {
        repo.deleteAll();
    }
}
