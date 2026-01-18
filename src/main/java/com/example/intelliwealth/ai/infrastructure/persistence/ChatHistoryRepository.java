package com.example.intelliwealth.ai.infrastructure.persistence;

import com.example.intelliwealth.ai.domain.ChatHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatHistoryRepository
        extends MongoRepository<ChatHistory, String> {
}

