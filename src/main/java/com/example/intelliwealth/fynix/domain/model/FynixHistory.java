package com.example.intelliwealth.fynix.domain.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "fynix_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FynixHistory {

    @Id
    private String id;
    @Column(nullable = false)
    private String conversationId;

    private String userId;

    private String query;

    private String context;

    private String response;

    private LocalDateTime createdAt;
}