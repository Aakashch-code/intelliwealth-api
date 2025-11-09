package com.example.intelliwealth.service;

import com.example.intelliwealth.model.Subscription;
import com.example.intelliwealth.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository repo;

    public List<Subscription> getAllSubscription() {
        return repo.findAll();
    }


    public Subscription getSubscriptionById(long subscriptionId) {
        return repo.findById(subscriptionId).orElse(new Subscription());
    }

    public Subscription updateSubscription(Subscription subscriptionId) {
        return repo.save(subscriptionId);
    }

    public Subscription createSubscription(Subscription subscription) {
        return repo.save(subscription);
    }

    public void deleteSubscriptionById(long subscriptionId) {
        repo.deleteById(subscriptionId);
    }

    public void deleteAllSubscription() {
        repo.deleteAll();
    }
}
