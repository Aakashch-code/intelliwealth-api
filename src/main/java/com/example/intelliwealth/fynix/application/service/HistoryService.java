package com.example.intelliwealth.fynix.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.fynix.application.dto.FynixChatRequest;
import com.example.intelliwealth.fynix.domain.model.FynixHistory;
import com.example.intelliwealth.fynix.infrastructure.persistence.FynixHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryService extends SecuredService {

    private final FynixHistoryRepository repository;

    // Added userId to the parameters to match the Controller call
    public void saveChat(
            String conversationId, // 🔥 receive it
            String question,
            String contextJson,
            String aiAnswer) {

        String userId = cacheKey();

        repository.save(
                FynixHistory.builder()
                        .userId(userId)
                        .conversationId(conversationId) // 🔥 use it
                        .query(question)
                        .context(contextJson)
                        .response(aiAnswer)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public List<FynixHistory> getMyHistory() {

        String userId = cacheKey();

        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<FynixHistory> getConversation(String conversationId){

        String userId = cacheKey();

        return repository
                .findByUserIdAndConversationIdOrderByCreatedAtAsc(
                        userId, conversationId
                );
    }



}