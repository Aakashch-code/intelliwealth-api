package com.example.intelliwealth.core.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findByIsActiveTrue();
    List<Subscription> findByIsActiveFalse();
}
