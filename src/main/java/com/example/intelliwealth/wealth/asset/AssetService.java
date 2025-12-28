package com.example.intelliwealth.wealth.asset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository repo;
    private final AssetsMapper mapper;

    @Transactional
    public AssetsResponseDTO createAsset(AssetsRequestDTO dto) {
        // 1. Initialize attributes if null to avoid NullPointer
        if (dto.getAttributes() == null) {
            dto.setAttributes(new HashMap<>());
        }

        // 2. Validate dynamic attributes
        AssetValidator.validateAttributes(dto.getCategory(), dto.getAttributes());

        // 3. Save
        Asset entity = mapper.toEntity(dto);
        Asset savedEntity = repo.save(entity);

        // 4. Return DTO
        return mapper.toDto(savedEntity);
    }

    public AssetsResponseDTO modifyAsset(AssetsRequestDTO dto, String id) {
        Asset existingAsset = repo.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset with ID " + id + " not found"));

        // Update fields
        existingAsset.setName(dto.getName());
        existingAsset.setMainCategory(dto.getMainCategory());
        existingAsset.setCategory(dto.getCategory());
        existingAsset.setCurrentValue(dto.getCurrentValue());
        existingAsset.setDateAcquired(dto.getDateAcquired());

        // Merge attributes carefully
        if (dto.getAttributes() != null) {
            if (existingAsset.getAttributes() == null) {
                existingAsset.setAttributes(new HashMap<>());
            }
            existingAsset.getAttributes().putAll(dto.getAttributes());
        }

        // Validate state after merge
        AssetValidator.validateAttributes(existingAsset.getCategory(), existingAsset.getAttributes());

        Asset savedEntity = repo.save(existingAsset);
        return mapper.toDto(savedEntity);
    }

    public List<AssetsResponseDTO> getAllAssets() {
        return repo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public AssetsResponseDTO getAssetById(String id) {
        Asset asset = repo.findById(id)
                .orElseThrow(() -> new AssetNotFoundException("Asset with ID " + id + " not found"));
        return mapper.toDto(asset);
    }

    public BigDecimal allAssetsAmount() {
        return repo.findAll().stream()
                .map(Asset::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteAsset(String id) {
        if (!repo.existsById(id)) {
            throw new AssetNotFoundException("Cannot delete. Asset with ID " + id + " not found");
        }
        repo.deleteById(id);
    }
}