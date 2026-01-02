package com.example.intelliwealth.fynix.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatHistory {

    @Id
    private String id;

    private String userQuery;

    private String aiResponse;

    private String summary;

    private Double confidenceScore;

    private LocalDateTime createdAt;
}

