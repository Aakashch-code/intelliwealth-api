package com.example.intelliwealth.protection.insurance.domain.repository;

import com.example.intelliwealth.protection.insurance.domain.model.Insurance;
import com.example.intelliwealth.protection.insurance.domain.model.InsuranceCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsuranceRepository {

    // Basic CRUD
    Insurance save(Insurance insurance);
    Optional<Insurance> findById(String id);
    void delete(Insurance insurance);

    // Business Queries (Just the names, no @Query logic here)
    List<Insurance> findAllByUserId(UUID userId);
    List<Insurance> findByUserIdAndCategory(UUID userId, InsuranceCategory category);
    List<Insurance> findActivePoliciesForUser(UUID userId, LocalDate date);
    List<Insurance> findByUserIdAndEndDateBetween(UUID userId, LocalDate start, LocalDate end);
}