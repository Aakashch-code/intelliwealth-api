package com.example.intelliwealth.fynix.infrastructure.persistence;

import com.example.intelliwealth.fynix.domain.ChatHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatHistoryRepository
        extends MongoRepository<ChatHistory, String> {
}

