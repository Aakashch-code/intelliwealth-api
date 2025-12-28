package com.example.intelliwealth.wealth.asset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository repo;
    private final AssetsMapper mapper;

    // ---------------- CREATE ----------------
    public Asset createAsset(AssetsRequestDTO dto) {

        if (dto.getAttributes() == null) {
            dto.setAttributes(new HashMap<>());
        }

        // STRICT validation for create
        AssetValidator.validateAttributes(
                dto.getCategory(),
                dto.getAttributes()
        );

        return repo.save(mapper.toEntity(dto));
    }

    // ---------------- UPDATE ----------------
    public Asset modifyAsset(AssetsRequestDTO dto, String id) {

        Asset asset = getAssetById(id);

        asset.setName(dto.getName());
        asset.setMainCategory(dto.getMainCategory());
        asset.setCategory(dto.getCategory());
        asset.setCurrentValue(dto.getCurrentValue());
        asset.setDateAcquired(dto.getDateAcquired());

        if (dto.getAttributes() != null) {
            if (asset.getAttributes() == null) {
                asset.setAttributes(new HashMap<>());
            }
            asset.getAttributes().putAll(dto.getAttributes());
        }

        // Validate AFTER merge
        AssetValidator.validateAttributes(
                asset.getCategory(),
                asset.getAttributes()
        );

        return repo.save(asset);
    }

    // ---------------- OTHERS ----------------
    public List<AssetsResponseDTO> getAllAssets() {
        return repo.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public Asset getAssetById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    public BigDecimal allAssetsAmount() {
        return repo.findAll()
                .stream()
                .map(Asset::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteAsset(String id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Asset not found");
        }
        repo.deleteById(id);
    }
}
