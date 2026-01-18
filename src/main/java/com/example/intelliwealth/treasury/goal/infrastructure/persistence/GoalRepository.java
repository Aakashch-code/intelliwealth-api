package com.example.intelliwealth.core.goal.infrastructure.persistence;

import com.example.intelliwealth.core.goal.domain.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findAllByUserId(UUID userId);
    Optional<Goal> findByIdAndUserId(long id, UUID userId);
    void deleteAllByUserId(UUID userId);
}