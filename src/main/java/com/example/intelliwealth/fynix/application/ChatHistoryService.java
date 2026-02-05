package com.example.intelliwealth.fynix.application;

import com.example.intelliwealth.fynix.api.dto.FynixResponse;
import com.example.intelliwealth.fynix.domain.ChatHistory;
import com.example.intelliwealth.fynix.infrastructure.persistence.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {

    private final ChatHistoryRepository repository;

    public void save(String query, FynixResponse response) {

        ChatHistory chat = ChatHistory.builder()
                .userQuery(query)
                .aiResponse(response.getExplanation())
                .summary(response.getSummary())
                .confidenceScore(calculateConfidence(response))
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(chat);
    }

    private double calculateConfidence(FynixResponse response) {
        if (response.getRecommendations() == null) return 0.3;
        return Math.min(1.0, 0.5 + response.getRecommendations().size() * 0.1);
    }
}

