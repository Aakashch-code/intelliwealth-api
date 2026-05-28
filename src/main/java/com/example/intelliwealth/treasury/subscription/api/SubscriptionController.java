package com.example.intelliwealth.treasury.subscription.api;

import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionRequest;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionResponse;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionStat;
import com.example.intelliwealth.treasury.subscription.application.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscription Management", description = "Manage user subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    @Operation(summary = "Get all subscriptions")
    @GetMapping
    public Page<SubscriptionResponse> getAll(
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        if (active != null) {
            return active
                    ? service.getActiveSubscriptions(pageable)
                    : service.getInactiveSubscriptions(pageable);
        }

        return service.getAllSubscriptions(pageable);
    }

    @Operation(summary = "Create subscription")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse create(
            @Valid @RequestBody SubscriptionRequest dto) {

        return service.createSubscription(dto);
    }

    @Operation(summary = "Get subscription by ID")
    @GetMapping("/{id}")
    public SubscriptionResponse getById(@PathVariable Long id) {

        return service.getSubscriptionById(id);
    }

    @Operation(summary = "Toggle subscription status")
    @PutMapping("/{id}/toggle")
    public SubscriptionResponse toggle(@PathVariable Long id) {

        return service.toggleSubscriptionStatus(id);
    }

    @Operation(summary = "Delete subscription")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        service.hardDeleteSubscription(id);
    }

    @Operation(summary = "Get subscription statistics")
    @GetMapping("/stats")
    public SubscriptionStat fetchStats() {

        return service.getStats();
    }
}