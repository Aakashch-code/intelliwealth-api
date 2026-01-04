package com.example.intelliwealth.protection.insurance.repository;

import com.example.intelliwealth.protection.insurance.domain.Insurance;
import com.example.intelliwealth.protection.insurance.domain.InsuranceCategory;
import com.example.intelliwealth.protection.insurance.domain.InsuranceMainCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsuranceRepository extends MongoRepository<Insurance, String> {

    // Find by specific sub-category (e.g., Term Life, Health)
    List<Insurance> findByCategory(InsuranceCategory category);

    // Find by main category (e.g., LIFE, GENERAL)
    List<Insurance> findByMainCategory(InsuranceMainCategory mainCategory);

    // Find policies expiring soon (useful for dashboard alerts)
    List<Insurance> findByEndDateBetween(LocalDate start, LocalDate end);

    // Custom Query: Find active policies (Start date <= today AND End date >= today)
    @Query("{ 'startDate': { $lte: ?0 }, 'endDate': { $gte: ?0 } }")
    List<Insurance> findActivePolicies(LocalDate refDate);
}