package com.example.intelliwealth.fynix.util;

import com.example.intelliwealth.fynix.dto.FynixResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiResponseParser {

    private final ObjectMapper objectMapper;

    public AiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public FynixResponse parse(String raw) {

        try {
            String cleanJson = AiJsonExtractor.extractJson(raw);
            return objectMapper.readValue(cleanJson, FynixResponse.class);

        } catch (Exception e) {

            FynixResponse fallback = new FynixResponse();
            fallback.setSummary("AI response could not be parsed.");
            fallback.setExplanation(raw);
            fallback.setRecommendations(List.of());
            fallback.setGoalAlignment(List.of());

            return fallback;
        }
    }
}
