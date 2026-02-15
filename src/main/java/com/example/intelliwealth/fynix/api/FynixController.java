package com.example.intelliwealth.fynix.api;

import com.example.intelliwealth.fynix.application.dto.FynixChatRequest;
import com.example.intelliwealth.fynix.application.dto.FynixChatResponse;
import com.example.intelliwealth.fynix.application.dto.FynixSummary;
import com.example.intelliwealth.fynix.application.service.HistoryService;
import com.example.intelliwealth.fynix.application.service.LLMService;
import com.example.intelliwealth.fynix.application.service.ChatService;
import com.example.intelliwealth.fynix.domain.model.FynixHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fynix")
@RequiredArgsConstructor
public class FynixController {

    private final ChatService chatService;
    private final HistoryService historyService;
    private final LLMService LLMService;

    @GetMapping("/context")
    public ResponseEntity<FynixSummary> getLLMContext() {

        FynixSummary context = chatService.generateContext();
        return ResponseEntity.ok(context);
    }
    @GetMapping("/history")
    public List<FynixHistory> getAllChats() {
        return historyService.getMyHistory();
    }
    @GetMapping("/history/conversation/{id}")
    public ResponseEntity<List<FynixHistory>> getConversation(
            @PathVariable String id){

        return ResponseEntity.ok(
                historyService.getConversation(id)
        );
    }


    @PostMapping("/chat")
    public ResponseEntity<FynixChatResponse> chatWithFynix(
            @RequestBody FynixChatRequest request) {

        try {

            String conversationId = request.getConversationId();

            // Generate ONLY if first message
            if (conversationId == null || conversationId.isBlank()) {
                conversationId = UUID.randomUUID().toString();
            }

            String contextJson = LLMService.generateFinancialContext();

            String aiAnswer =
                    LLMService.getFinancialAdvice(request.getQuery());

            historyService.saveChat(
                    conversationId, // 🔥 pass it
                    request.getQuery(),
                    contextJson,
                    aiAnswer
            );

            return ResponseEntity.ok(
                    new FynixChatResponse(aiAnswer, conversationId)
            );

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body(new FynixChatResponse(
                    ));
        }
    }

}