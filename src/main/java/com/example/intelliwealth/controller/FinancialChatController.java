package com.example.intelliwealth.controller;

import com.example.intelliwealth.service.FinancialChatService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class FinancialChatController {

    @Autowired
    private FinancialChatService financialChatService;

    @Getter
    static class ChatRequest {
        // getter and setter
        private String query;

        public void setQuery(String query) { this.query = query; }
    }

    @PostMapping("/")
    public String handleChat(@RequestBody ChatRequest request) {
        return financialChatService.getPersonalizedAdvice(request.getQuery());
    }
}