package com.example.intelliwealth.ai.api;

import com.example.intelliwealth.ai.api.dto.FynixRequest;
import com.example.intelliwealth.ai.api.dto.FynixResponse;
import com.example.intelliwealth.ai.application.FinancialChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Fynix AI", description = "AI-powered financial insights and analysis")
public class FinancialChatController {

    private final FinancialChatService chatService;

    @Operation(
            summary = "Analyze financial data",
            description = "Processes user financial input and returns AI-generated insights"
    )
    @PostMapping("/analyze")
    public ResponseEntity<FynixResponse> analyze(
            @RequestBody FynixRequest request
    ) {
        return ResponseEntity.ok(chatService.generateAdvice(request));
    }
}
