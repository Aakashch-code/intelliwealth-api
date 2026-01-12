package com.example.intelliwealth.wealth.asset.infrastructure.persistence;

import com.example.intelliwealth.wealth.asset.domain.model.Asset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataAssetRepository extends MongoRepository<Asset, String> {

    List<Asset> findAllByUserId(UUID userId);

    Optional<Asset> findByIdAndUserId(String id, UUID userId);

    long deleteByIdAndUserId(String id, UUID userId);
}
