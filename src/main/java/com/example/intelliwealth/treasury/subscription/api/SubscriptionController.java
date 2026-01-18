package com.example.intelliwealth.treasury.subscription.api;

import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionRequestDTO;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionResponseDTO;
import com.example.intelliwealth.treasury.subscription.application.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Subscription Management", description = "Manage user subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    // --- Endpoints ---

    @Operation(summary = "Get all subscriptions", description = "Fetch all, or filter using ?active=true/false")
    @GetMapping
    public List<SubscriptionResponseDTO> getAll(
            @Parameter(description = "Filter by status") @RequestParam(required = false) Boolean active) {
        if (active != null) {
            return active ? service.getActiveSubscriptions() : service.getInactiveSubscriptions();
        }
        return service.getAllSubscriptions();
    }

    @Operation(summary = "Create subscription")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponseDTO create(@RequestBody SubscriptionRequestDTO dto) {
        return service.createSubscription(dto);
    }

    @Operation(summary = "Get by ID")
    @GetMapping("/{id}")
    public SubscriptionResponseDTO getById(@PathVariable Long id) {
        return service.getSubscriptionById(id);
    }

    @Operation(summary = "Toggle Status", description = "Pauses or Resumes a subscription")
    @PutMapping("/{id}/toggle")
    public SubscriptionResponseDTO toggle(@PathVariable Long id) {
        return service.toggleSubscriptionStatus(id);
    }

    @Operation(summary = "Delete permanently")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.hardDeleteSubscription(id);
    }
}