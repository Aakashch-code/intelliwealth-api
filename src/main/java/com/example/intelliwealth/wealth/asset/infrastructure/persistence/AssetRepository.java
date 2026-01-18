package com.example.intelliwealth.wealth.asset.domain.repository;

import com.example.intelliwealth.wealth.asset.domain.model.Asset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository {
    Asset save(Asset asset);
    List<Asset> findAllByUserId(UUID userId);
    Optional<Asset> findByIdAndUserId(String id, UUID userId);
    void deleteByIdAndUserId(String id, UUID userId);


}