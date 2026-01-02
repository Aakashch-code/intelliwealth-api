package com.example.intelliwealth.fynix.repository;

import com.example.intelliwealth.fynix.model.ChatHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatHistoryRepository
        extends MongoRepository<ChatHistory, String> {
}

