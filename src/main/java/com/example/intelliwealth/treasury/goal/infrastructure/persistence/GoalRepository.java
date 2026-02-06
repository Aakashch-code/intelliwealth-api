package com.example.intelliwealth.treasury.goal.infrastructure.persistence;

import com.example.intelliwealth.treasury.goal.domain.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUserId(UUID userId);
    Optional<Goal> findByIdAndUserId(long id, UUID userId);
    void deleteAllByUserId(UUID userId);

    @Query("select coalesce(sum(g.targetAmount), 0) from Goal g where g.userId = :userId")
    BigDecimal sumTargetAmountByUserId(@Param("userId") UUID userId);
    @Query("select coalesce(sum(g.currentAmount),0) from Goal g where g.userId = :userId ")
    BigDecimal sumCurrentAmountByUserId(@Param("userId") UUID userId);
}