package com.example.intelliwealth.ai.api.dto;

import lombok.Data;

@Data
public class GoalAlignment {

    private String goalId;

    private String goalName;

    private String alignment; // Aligned | Partially aligned | Not aligned

    private String notes;
}
