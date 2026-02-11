package com.example.intelliwealth.treasury.subscription.infrastrcture.persistence;

import com.example.intelliwealth.treasury.subscription.domain.model.BillingCycle;
import com.example.intelliwealth.treasury.subscription.domain.model.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    Page<Subscription> findAllByUserId(UUID userId , Pageable pageable);

    Optional<Subscription> findByIdAndUserId(Long id, UUID userId);

    Page<Subscription> findByUserIdAndIsActiveTrue(UUID userId, Pageable pageable);

    Page<Subscription> findByUserIdAndIsActiveFalse(UUID userId, Pageable pageable);

        @Query("""
        SELECT COALESCE(SUM(s.amount), 0)
        FROM Subscription s
        WHERE s.userId = :userId
          AND s.isActive = true
          AND s.billingCycle = :cycle
    """)
        BigDecimal calculateTotalByUserIdAndCycle(
                @Param("userId") UUID userId,
                @Param("cycle") BillingCycle cycle
        );
    @Query("""
    SELECT s.billingCycle, COALESCE(SUM(s.amount), 0)
    FROM Subscription s
    WHERE s.userId = :userId
      AND s.isActive = true
    GROUP BY s.billingCycle
""")
    List<Object[]> sumByCycle(@Param("userId") UUID userId);


}