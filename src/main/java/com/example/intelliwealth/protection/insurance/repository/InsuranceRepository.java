package com.example.intelliwealth.protection.insurance.repository;

import com.example.intelliwealth.protection.insurance.domain.Insurance;
import com.example.intelliwealth.protection.insurance.domain.InsuranceCategory;
import com.example.intelliwealth.protection.insurance.domain.InsuranceMainCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface InsuranceRepository extends MongoRepository<Insurance, String> {

    // ✅ Find ALL policies for a specific user
    List<Insurance> findAllByUserId(UUID userId);

    // ✅ Find by Category for a specific user
    List<Insurance> findByUserIdAndCategory(UUID userId, InsuranceCategory category);

    // ✅ Find Active policies for a specific user
    @Query("{'userId': ?0, 'endDate': {$gte: ?1}}")
    List<Insurance> findActivePoliciesForUser(UUID userId, LocalDate date);

    // ✅ Find Expiring policies for a specific user
    List<Insurance> findByUserIdAndEndDateBetween(UUID userId, LocalDate start, LocalDate end);
}