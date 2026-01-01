package com.example.intelliwealth.protection.insurance;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InsuranceRepository
        extends MongoRepository<Insurance, String> {
}
