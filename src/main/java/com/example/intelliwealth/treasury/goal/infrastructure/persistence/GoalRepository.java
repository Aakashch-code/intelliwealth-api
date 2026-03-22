package com.example.intelliwealth.treasury.goal.infrastructure.persistence;

import com.example.intelliwealth.treasury.goal.application.dto.GoalMinimal;
import com.example.intelliwealth.treasury.goal.application.dto.GoalStat;
import com.example.intelliwealth.treasury.goal.domain.model.Goal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findAllByUserId(UUID userId, Pageable pageable);
    @Query("""
SELECT new com.example.intelliwealth.treasury.goal.application.dto.GoalMinimal(
       g.targetAmount,
       g.currentAmount,
       g.targetDate
)
FROM Goal g
WHERE g.userId = :userId
""")
    List<GoalMinimal> findMinimalGoal(UUID userId);

    Optional<Goal> findByIdAndUserId(long id, UUID userId);
    void deleteAllByUserId(UUID userId);

    @Query("""
SELECT new com.example.intelliwealth.treasury.goal.application.dto.GoalStat(

   COUNT(g),

   SUM(CASE WHEN g.currentAmount >= g.targetAmount
            THEN 1 ELSE 0 END),

   CAST(COALESCE(SUM(g.targetAmount), 0) AS bigdecimal),

   CAST(COALESCE(SUM(g.currentAmount), 0) AS bigdecimal )

)
FROM Goal g
WHERE g.userId = :userId
""")
    GoalStat getStats(UUID userId);



}