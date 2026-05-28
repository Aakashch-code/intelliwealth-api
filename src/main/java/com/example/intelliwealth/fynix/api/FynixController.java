package com.example.intelliwealth.fynix.api;

import com.example.intelliwealth.fynix.application.dto.FynixChatRequest;
import com.example.intelliwealth.fynix.application.dto.FynixChatResponse;
import com.example.intelliwealth.fynix.application.dto.FynixSummary;
import com.example.intelliwealth.fynix.application.service.ChatService;
import com.example.intelliwealth.fynix.application.service.HistoryService;
import com.example.intelliwealth.fynix.application.service.LLMService;
import com.example.intelliwealth.fynix.domain.model.FynixHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fynix")
@RequiredArgsConstructor
@Tag(
        name = "Fynix AI Assistant",
        description = "AI-powered financial insights, chat, and history management"
)public class FynixController {

    private final ChatService chatService;
    private final HistoryService historyService;
    private final LLMService llmService;

    @Operation(summary = "Get financial summary")
    @GetMapping("/context")
    public ResponseEntity<FynixSummary> getLLMContext() {

        return ResponseEntity.ok(
                chatService.generateContext()
        );
    }

    @Operation(summary = "Get chat history")
    @GetMapping("/history")
    public List<FynixHistory> getAllChats() {

        return historyService.getMyHistory();
    }

    @Operation(summary = "Get conversation history")
    @GetMapping("/history/conversation/{id}")
    public ResponseEntity<List<FynixHistory>> getConversation(
            @PathVariable String id) {

        return ResponseEntity.ok(
                historyService.getConversation(id)
        );
    }

    @Operation(summary = "Chat with Fynix")
    @PostMapping("/chat")
    public ResponseEntity<FynixChatResponse> chatWithFynix(
            @RequestBody FynixChatRequest request) {

        try {

            String conversationId = request.getConversationId();

            if (conversationId == null || conversationId.isBlank()) {
                conversationId = UUID.randomUUID().toString();
            }

            String contextJson =
                    llmService.generateFinancialContext();

            String aiAnswer =
                    llmService.getFinancialAdvice(request.getQuery());

            historyService.saveChat(
                    conversationId,
                    request.getQuery(),
                    contextJson,
                    aiAnswer
            );

            return ResponseEntity.ok(
                    new FynixChatResponse(aiAnswer, conversationId)
            );

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body(new FynixChatResponse());
        }
    }
}