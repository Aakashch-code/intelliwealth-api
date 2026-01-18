package com.example.intelliwealth.treasury.subscription.infrastrcture.persistence;

import com.example.intelliwealth.treasury.subscription.domain.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findAllByUserId(UUID userId);

    Optional<Subscription> findByIdAndUserId(Long id, UUID userId);

    List<Subscription> findByUserIdAndIsActiveTrue(UUID userId);

    List<Subscription> findByUserIdAndIsActiveFalse(UUID userId);
}