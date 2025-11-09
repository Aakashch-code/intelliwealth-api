package com.example.intelliwealth.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.intelliwealth.model.Budget;
import com.example.intelliwealth.model.Subscription;
import com.example.intelliwealth.model.Transaction;
import com.example.intelliwealth.model.Goal;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class FinancialChatService {

    private final ChatClient chatClient;
    private final BudgetService budgetService;
    private final SubscriptionService subscriptionService;
    private final TransactionService transactionService;
    private final GoalService goalService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public FinancialChatService(ChatClient.Builder chatClientBuilder,
                                BudgetService budgetService,
                                SubscriptionService subscriptionService,
                                TransactionService transactionService,
                                GoalService goalService) {
        // build ChatClient from the autoconfigured builder
        this.chatClient = chatClientBuilder.build();
        this.budgetService = budgetService;
        this.subscriptionService = subscriptionService;
        this.transactionService = transactionService;
        this.goalService = goalService;
    }

    public String getPersonalizedAdvice(String userQuery) {

        // 1) Fetch all four datasets
        List<Budget> budgets = budgetService.getAllBudget();
        List<Subscription> subscriptions = subscriptionService.getAllSubscription();
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<Goal> goals = goalService.getAllGoal();

        // 2) Serialize them (pretty printed so the model can read easily)
        String budgetsJson = "[]";
        String subsJson = "[]";
        String txJson = "[]";
        String goalsJson = "[]";
        try {
            budgetsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(budgets);
            subsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(subscriptions);
            txJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transactions);
            goalsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(goals);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String finalPrompt = ""
                + "You are 'IntelliWealth Assistant called Fynix AI' — an expert personal finance assistant.\n"
                + "IMPORTANT RULES (must follow exactly):\n"
                + "1) Use ONLY the data provided in the ---DATA CONTEXT--- below. Do NOT invent facts or infer values not present.\n"
                + "2) If a calculation or estimate is required but some input is missing, state clearly which fields are missing and provide a conservative estimate range using only available data.\n"
                + "3) Provide a concise, prioritized action plan and an estimated monthly impact for each recommendation.\n"
                + "4) Output MUST be valid JSON only (no extra text). The JSON schema required is specified below.\n"
                + "\n"
                + "--- REQUIRED JSON SCHEMA ---\n"
                + "{\n"
                + "  \"summary\": \"short one-paragraph summary of the user's state\",\n"
                + "  \"key_metrics\": {\n"
                + "     \"total_monthly_income\": number_or_null,\n"
                + "     \"total_monthly_spend\": number_or_null,\n"
                + "     \"monthly_subscription_cost\": number_or_null,\n"
                + "     \"savings_rate\": number_or_null // percent\n"
                + "  },\n"
                + "  \"recommendations\": [\n"
                + "     {\n"
                + "       \"id\": \"rec-1\",\n"
                + "       \"title\": \"short title\",\n"
                + "       \"description\": \"1-2 sentence explanation\",\n"
                + "       \"priority\": \"High|Medium|Low\",\n"
                + "       \"estimated_monthly_impact\": number_or_null, // positive = reduce spend / increase net savings\n"
                + "       \"confidence\": \"High|Medium|Low\",\n"
                + "       \"actions\": [\"actionable step 1\", \"actionable step 2\"]\n"
                + "     }\n"
                + "  ],\n"
                + "  \"goal_alignment\": [\n"
                + "     {\n"
                + "       \"goal_id\": \"<id>\",\n"
                + "       \"goal_name\": \"<name>\",\n"
                + "       \"alignment\": \"Aligned|Partially aligned|Not aligned\",\n"
                + "       \"notes\": \"how current budget/subscriptions/transactions affect this goal\"\n"
                + "     }\n"
                + "  ],\n"
                + "  \"missing_data\": [\"list of missing fields if any\"],\n"
                + "  \"follow_up_questions\": [\"If more input needed, ask explicit questions here\"],\n"
                + "  \"explanation\": \"(optional detailed explanation, up to 200 words)\"\n"
                + "}\n"
                + "\n"
                + "--- DATA CONTEXT ---\n"
                + "BUDGETS:\n" + budgetsJson + "\n\n"
                + "SUBSCRIPTIONS:\n" + subsJson + "\n\n"
                + "TRANSACTIONS:\n" + txJson + "\n\n"
                + "GOALS:\n" + goalsJson + "\n\n"
                + "--- END OF DATA ---\n\n"
                + "User's Question: \"" + userQuery + "\"\n\n"
                + "Additional instructions:\n"
                + "- Keep numerical outputs as numbers (not strings). If value unknown use null.\n"
                + "- For monetary values, use the user's currency when available; otherwise return values as numeric without currency symbol.\n"
                + "- Keep recommendations to max 6 items, prioritized.\n"
                + "- If the provided data is large, summarize only the most impactful items (top 5 by amount or frequency).\n"
                + "- DO NOT output any commentary outside the required JSON. The response must be a single JSON object.\n";

        // 4) Send to model and return content
        return chatClient.prompt()
                .user(finalPrompt)
                .call()
                .content();
    }
}
