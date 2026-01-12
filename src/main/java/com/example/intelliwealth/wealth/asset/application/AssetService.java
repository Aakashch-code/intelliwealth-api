package com.example.intelliwealth.wealth.asset.application;

import com.example.intelliwealth.authentication.security.SecuredService;
import com.example.intelliwealth.wealth.asset.domain.model.Asset;
import com.example.intelliwealth.wealth.asset.api.dto.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.api.dto.AssetsResponseDTO;
import com.example.intelliwealth.wealth.asset.domain.exception.AssetNotFoundException;
import com.example.intelliwealth.wealth.asset.application.mapper.AssetsMapper;
import com.example.intelliwealth.wealth.asset.domain.repository.AssetRepository;
import com.example.intelliwealth.wealth.asset.domain.rules.AssetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class AssetService extends SecuredService {

    private final AssetRepository repo;
    private final AssetsMapper mapper;

    // ---------------- CREATE ----------------

    public AssetsResponseDTO createAsset(AssetsRequestDTO dto) {

        if (dto.getAttributes() == null) {
            dto.setAttributes(new HashMap<>());
        }

        AssetValidator.validateAttributes(dto.getCategory(), dto.getAttributes());

        Asset entity = mapper.toEntity(dto);
        entity.setUserId(currentUserId());

        return mapper.toDto(repo.save(entity));
    }

    // ---------------- UPDATE ----------------

    public AssetsResponseDTO modifyAsset(AssetsRequestDTO dto, String id) {

        Asset existing = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new AssetNotFoundException("Asset not found"));

        existing.setName(dto.getName());
        existing.setMainCategory(dto.getMainCategory());
        existing.setCategory(dto.getCategory());
        existing.setCurrentValue(dto.getCurrentValue());
        existing.setDateAcquired(dto.getDateAcquired());

        if (dto.getAttributes() != null) {
            existing.getAttributes().putAll(dto.getAttributes());
        }

        AssetValidator.validateAttributes(existing.getCategory(), existing.getAttributes());

        return mapper.toDto(repo.save(existing));
    }

    // ---------------- READ ----------------

    public List<AssetsResponseDTO> getAllAssets() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public AssetsResponseDTO getAssetById(String id) {
        return repo.findByIdAndUserId(id, currentUserId())
                .map(mapper::toDto)
                .orElseThrow(() -> new AssetNotFoundException("Asset not found"));
    }

    // ---------------- AGGREGATE ----------------

    public BigDecimal allAssetsAmount() {
        return repo.findAllByUserId(currentUserId())
                .stream()
                .map(Asset::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------------- DELETE ----------------

    public void deleteAsset(String id) {
        repo.deleteByIdAndUserId(id, currentUserId());
    }

}
