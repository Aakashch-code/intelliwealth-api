package com.example.intelliwealth.treasury.subscription.api;

import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionRequest;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionResponse;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionStat;
import com.example.intelliwealth.treasury.subscription.application.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Get all subscriptions", description = "Fetch all, or filter using ?active=true/false")
    @GetMapping
    public Page<SubscriptionResponse> getAll(
            @Parameter(description = "Filter by status") @RequestParam(required = false) Boolean active , Pageable pageable) {
        if (active != null) {
            return active ? service.getActiveSubscriptions(pageable) : service.getInactiveSubscriptions(pageable);
        }
        return service.getAllSubscriptions(pageable);
    }
    @Operation(summary = "Create subscription")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse create(@RequestBody SubscriptionRequest dto) {
        return service.createSubscription(dto);
    }
    @Operation(summary = "Get by ID")
    @GetMapping("/{id}")
    public SubscriptionResponse getById(@PathVariable Long id) {
        return service.getSubscriptionById(id);
    }

    @Operation(summary = "Toggle Status", description = "Pauses or Resumes a subscription")
    @PutMapping("/{id}/toggle")
    public SubscriptionResponse toggle(@PathVariable Long id) {
        return service.toggleSubscriptionStatus(id);
    }

    @Operation(summary = "Delete permanently")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.hardDeleteSubscription(id);
    }

    @GetMapping("/stat")
    @Operation(summary = "Get subscription stats")
    public SubscriptionStat fetchStats() {
        return service.getStats();
    }

}