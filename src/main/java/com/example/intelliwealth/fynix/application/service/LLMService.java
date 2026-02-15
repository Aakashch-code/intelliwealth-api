package com.example.intelliwealth.fynix.application.service;

import com.example.intelliwealth.fynix.application.dto.GeminiRequest;
import com.example.intelliwealth.fynix.application.dto.GeminiResponse;
import com.example.intelliwealth.fynix.domain.exception.TooManyRequestsException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class LLMService {

    private final ChatService chatService;
    private final ObjectMapper yamlMapper;
    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    public LLMService(ChatService chatService,ObjectMapper objectMapper, WebClient.Builder webClientBuilder) {
        this.chatService = chatService;

        this.yamlMapper = objectMapper.copy(); // Copy it so we don't mess up your REST APIs
        this.yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Drop nulls!
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    /**
     * Main public API
     */
    public String getFinancialAdvice(String userQuestion) {
        try {
            String context = generateFinancialContext();
            String prompt = buildPrompt(context, userQuestion);

            GeminiRequest request = GeminiRequest.fromText(prompt);
            GeminiResponse response = callGemini(request);

            return extractAnswer(response);

        } catch (Exception ex) {
            ex.printStackTrace(); // TEMP DEBUG
            throw new RuntimeException("Fynix AI failed: " + ex.getMessage(), ex);
        }
    }

    /**
     * Builds YAML context to drastically save LLM tokens
     */
    public String generateFinancialContext() throws JsonProcessingException {
        // Fetch the raw DTO
        Object contextData = chatService.generateContext();

        // Convert it to a minified YAML string (ignoring nulls)
        return yamlMapper.writeValueAsString(contextData);
    }

    /**
     * Builds professional prompt
     */
    private String buildPrompt(String context, String question) {
        return """
You are Fynix, a calm, practical, and professional financial advisor.

Tone & Style Rules:
- Use the ₹ symbol for currency.
- Be clear, supportive, and realistic.
- Do NOT exaggerate or use dramatic language.
- Avoid fear-based wording (e.g., "extremely dangerous", "critical crisis").
- Keep advice practical and achievable.
- Keep your response under 3 short paragraphs.
- Provide exactly 3 bullet points for Priority Actions.
- Focus only on actionable advice.

Behavior Rules:
- Prioritize immediate, short-term actions (what to do this week/month).
- Suggest realistic emergency fund targets (step-by-step).
- If expenses are high, suggest specific reductions.
- Do not repeat financial data unnecessarily.
- Do not include disclaimers.

Structure:
1. Key Risks (2–3 concise bullets)
2. Priority Actions (3 specific, actionable steps)
3. Next Steps (2 clear follow-ups)

FINANCIAL DATA:
%s

USER QUESTION:
%s
""".formatted(context, question);
    }

    /**
     * Calls Gemini API
     */
    private GeminiResponse callGemini(GeminiRequest request) {
        try {
            return webClient.post()
                    .uri("/v1beta/models/" + model + ":generateContent")
                    .header("X-goog-api-key", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();

        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().value() == 429) {
                throw new TooManyRequestsException("AI quota exceeded. Try again later.");
            }
            throw new RuntimeException("Gemini API error: " + ex.getResponseBodyAsString(), ex);
        }
    }

    /**
     * Extracts AI text safely
     */
    private String extractAnswer(GeminiResponse response) {
        if (response == null ||
                response.candidates() == null ||
                response.candidates().isEmpty()) {
            throw new RuntimeException("Empty AI response");
        }

        return response.candidates()
                .getFirst()
                .content()
                .parts()
                .getFirst()
                .text()
                .trim();
    }
}