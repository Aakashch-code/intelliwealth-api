package com.example.intelliwealth.controller;

import com.example.intelliwealth.model.Goal;
import com.example.intelliwealth.model.Subscription;
import com.example.intelliwealth.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")

public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    //Get
    @GetMapping("/subscription")
    public List<Subscription> getAllSubscription() {
        return service.getAllSubscription();
    }

    @GetMapping("/subscription/{subscriptionId}")
    public Subscription getSubscriptionById(@PathVariable int subscriptionId) {
        return service.getSubscriptionById(subscriptionId);
    }

    //Put
    @PutMapping("/subscription/{subscriptionId}")
    public Subscription updateSubscriptionById (@RequestBody Subscription subscriptionId) {
        return service.updateSubscription(subscriptionId);
    }
    //Post
    @PostMapping("/subscription")
    public Subscription createSubscription (@RequestBody Subscription subscription) {
        return service.createSubscription(subscription);
    }

    //Delete
    @DeleteMapping("/subscription/{subscriptionId}")
    public void deleteSubscription (@PathVariable long subscriptionId) {
        service.deleteSubscriptionById(subscriptionId);
    }
    @DeleteMapping("/subscription")
    public void deleteAllSubscription () {
        service.deleteAllSubscription();
    }


}
