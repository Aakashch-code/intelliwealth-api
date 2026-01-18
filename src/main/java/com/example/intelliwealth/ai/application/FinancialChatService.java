package com.example.intelliwealth.fynix.application;

import com.example.intelliwealth.fynix.api.dto.FinancialSummary;
import com.example.intelliwealth.fynix.api.dto.FynixRequest;
import com.example.intelliwealth.fynix.api.dto.FynixResponse;
import com.example.intelliwealth.fynix.infrastructure.ai.AiResponseParser;
import com.example.intelliwealth.fynix.infrastructure.ai.FynixPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialChatService {

    private final FinancialSummaryService summaryService;
    private final FynixPromptBuilder promptBuilder;
    private final ChatClient chatClient;
    private final AiResponseParser parser;
    private final ChatHistoryService chatHistoryService;

    public FynixResponse generateAdvice(FynixRequest request) {

        FinancialSummary summary = summaryService.buildSummary();

        String prompt = promptBuilder.buildPrompt(
                request.getQuery(),
                summary
        );

        String rawResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        FynixResponse response = parser.parse(rawResponse);

        chatHistoryService.save(request.getQuery(), response);

        return response;
    }
}
