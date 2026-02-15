package com.example.intelliwealth.fynix.application.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FynixChatRequest {
    private String query;
    private String conversationId;
}