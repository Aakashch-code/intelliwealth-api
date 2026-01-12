package com.example.intelliwealth.wealth.asset.infrastructure.persistence;

import com.example.intelliwealth.wealth.asset.domain.exception.AssetNotFoundException;
import com.example.intelliwealth.wealth.asset.domain.model.Asset;
import com.example.intelliwealth.wealth.asset.domain.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AssetRepositoryImpl implements AssetRepository {

    private final SpringDataAssetRepository springDataRepo;

    @Override
    public Asset save(Asset asset) {
        return springDataRepo.save(asset);
    }

    @Override
    public List<Asset> findAllByUserId(UUID userId) {
        return springDataRepo.findAllByUserId(userId);
    }

    @Override
    public Optional<Asset> findByIdAndUserId(String id, UUID userId) {
        return springDataRepo.findByIdAndUserId(id, userId);
    }

    @Override
    public void deleteByIdAndUserId(String id, UUID userId) {
        long deleted = springDataRepo.deleteByIdAndUserId(id, userId);

        if (deleted == 0) {
            throw new AssetNotFoundException(id);
        }
    }
}
