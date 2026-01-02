package com.example.intelliwealth.fynix.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "AI-generated financial analysis response")
public class FynixResponse {

    @Schema(example = "Your financial health is stable with room for optimization.")
    private String summary;

    @Schema(description = "Key financial metrics like income, expenses, net worth")
    private Map<String, Object> keyMetrics;

    @Schema(description = "AI-generated financial recommendations")
    private List<Recommendation> recommendations;

    @Schema(description = "Goal tracking and alignment info")
    private List<GoalAlignment> goalAlignment;

    @Schema(example = "Reduce discretionary spending to improve savings rate.")
    private String explanation;
}
