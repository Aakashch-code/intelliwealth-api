package com.example.intelliwealth.protection.insurance.infrastructure.persistence;

import com.example.intelliwealth.protection.insurance.domain.model.Insurance;
import com.example.intelliwealth.protection.insurance.domain.model.InsuranceCategory;
import com.example.intelliwealth.protection.insurance.domain.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InsuranceRepositoryImpl implements InsuranceRepository {

    private final SpringDataInsuranceRepository springRepo;

    @Override
    public Insurance save(Insurance insurance) {
        return springRepo.save(insurance);
    }

    @Override
    public Optional<Insurance> findById(String id) {
        return springRepo.findById(id);
    }

    @Override
    public void delete(Insurance insurance) {
        springRepo.delete(insurance);
    }

    @Override
    public List<Insurance> findAllByUserId(UUID userId) {
        return springRepo.findAllByUserId(userId);
    }

    @Override
    public List<Insurance> findByUserIdAndCategory(UUID userId, InsuranceCategory category) {
        return springRepo.findByUserIdAndCategory(userId, category);
    }

    @Override
    public List<Insurance> findActivePoliciesForUser(UUID userId, LocalDate date) {
        return springRepo.findActivePoliciesForUser(userId, date);
    }

    @Override
    public List<Insurance> findByUserIdAndEndDateBetween(UUID userId, LocalDate start, LocalDate end) {
        return springRepo.findByUserIdAndEndDateBetween(userId, start, end);
    }
}