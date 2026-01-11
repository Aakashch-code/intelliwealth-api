package com.example.intelliwealth.wealth.asset.service;

import com.example.intelliwealth.authentication.model.Users;
import com.example.intelliwealth.authentication.security.SecuredService;
import com.example.intelliwealth.wealth.asset.domain.Asset;
import com.example.intelliwealth.wealth.asset.dto.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.dto.AssetsResponseDTO;
import com.example.intelliwealth.wealth.asset.exception.AssetNotFoundException;
import com.example.intelliwealth.wealth.asset.mapper.AssetsMapper;
import com.example.intelliwealth.wealth.asset.repository.AssetRepository;
import com.example.intelliwealth.wealth.asset.validation.AssetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Asset asset = repo.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new AssetNotFoundException("Asset not found"));

        repo.delete(asset);
    }
}
