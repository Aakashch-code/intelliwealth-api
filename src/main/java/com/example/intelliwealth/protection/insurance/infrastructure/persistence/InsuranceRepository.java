package com.example.intelliwealth.protection.insurance.infrastructure.persistence;

import com.example.intelliwealth.protection.insurance.domain.model.Insurance;
import com.example.intelliwealth.protection.insurance.domain.model.InsuranceCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface InsuranceRepository extends MongoRepository<Insurance, String> {

    // Auto-derived queries
    List<Insurance> findAllByUserId(UUID userId);

    List<Insurance> findByUserIdAndCategory(UUID userId, InsuranceCategory category);

    List<Insurance> findByUserIdAndEndDateBetween(
            UUID userId,
            LocalDate start,
            LocalDate end
    );

    // Custom Mongo query
    @Query("{ 'userId': ?0, 'endDate': { $gte: ?1 } }")
    List<Insurance> findActivePoliciesForUser(UUID userId, LocalDate date);
}
