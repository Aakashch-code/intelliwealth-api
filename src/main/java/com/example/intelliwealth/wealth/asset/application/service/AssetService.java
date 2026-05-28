package com.example.intelliwealth.wealth.asset.application.service;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.wealth.asset.application.dto.MainCategorySumResult;
import com.example.intelliwealth.wealth.asset.domain.model.Asset;
import com.example.intelliwealth.wealth.asset.application.dto.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.application.dto.AssetsResponseDTO;
import com.example.intelliwealth.wealth.asset.domain.exception.AssetNotFoundException;
import com.example.intelliwealth.wealth.asset.infrastructure.mapper.AssetsMapper;
import com.example.intelliwealth.wealth.asset.infrastructure.persistence.AssetRepository;
import com.example.intelliwealth.wealth.asset.domain.rules.AssetValidator;
import com.example.intelliwealth.wealth.asset.application.dto.CategorySumResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@PreAuthorize("isAuthenticated()")
public class AssetService extends SecuredService {

    private static final int MAX_PAGE_SIZE = 100;

    private static final String CACHE_ASSET_SUM = "asset_sum";
    private static final String CACHE_CATEGORY_SUM = "asset_category_sum";
    private static final String CACHE_MAIN_CATEGORY_SUM = "asset_main_category_sum";

    private final AssetRepository assetRepository;
    private final AssetsMapper assetMapper;

    @CacheEvict(cacheNames = {CACHE_ASSET_SUM, CACHE_CATEGORY_SUM, CACHE_MAIN_CATEGORY_SUM}, key = "#root.target.currentUserId()")
    public AssetsResponseDTO createAsset(AssetsRequestDTO request) {
        if (request.getAttributes() == null) {
            request.setAttributes(new HashMap<>());
        }

        AssetValidator.validateAttributes(request.getCategory(), request.getAttributes());

        Asset asset = assetMapper.toEntity(request);
        asset.setUserId(currentUserId());

        return assetMapper.toDto(assetRepository.save(asset));
    }

    @CacheEvict(cacheNames = {CACHE_ASSET_SUM, CACHE_CATEGORY_SUM, CACHE_MAIN_CATEGORY_SUM}, key = "#root.target.currentUserId()")
    public AssetsResponseDTO modifyAsset(AssetsRequestDTO request, String id) {
        Asset existingAsset = findAssetOrThrow(id);

        assetMapper.updateEntityFromDto(request, existingAsset);
        AssetValidator.validateAttributes(existingAsset.getCategory(), existingAsset.getAttributes());

        return assetMapper.toDto(assetRepository.save(existingAsset));
    }

    @Transactional(readOnly = true)
    public Page<AssetsResponseDTO> getAllAssets(Pageable pageable) {

        // Default to the provided pageable (which could be unpaged)
        Pageable effectivePageable = pageable;

        // Only enforce the max page size if the request is actually requesting pagination
        if (pageable.isPaged()) {
            effectivePageable = PageRequest.of(
                    pageable.getPageNumber(),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }

        return assetRepository.findAllByUserId(currentUserId(), effectivePageable)
                .map(assetMapper::toDto);
    }

    @Transactional(readOnly = true)
    public AssetsResponseDTO getAssetById(String id) {
        return assetMapper.toDto(findAssetOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_ASSET_SUM, key = "#root.target.currentUserId()")
    public BigDecimal getTotalAssetValue() {
        Decimal128 totalAssetSum = assetRepository.sumAssetValueByUserId(currentUserId());
        return totalAssetSum != null ? totalAssetSum.bigDecimalValue() : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_CATEGORY_SUM, key = "#root.target.currentUserId()")
    public Map<String, BigDecimal> getTotalValueByCategory() {
        return assetRepository.sumByCategory(currentUserId()).stream()
                .filter(result -> result.getId() != null && result.getTotal() != null)
                .collect(Collectors.toMap(
                        CategorySumResult::getId,
                        CategorySumResult::getTotal,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_MAIN_CATEGORY_SUM, key = "#root.target.currentUserId()")
    public Map<String, BigDecimal> getTotalValueByMainCategory() {
        return assetRepository.sumByMainCategory(currentUserId()).stream()
                .filter(result -> result.getId() != null && result.getTotal() != null)
                .collect(Collectors.toMap(
                        MainCategorySumResult::getId,
                        MainCategorySumResult::getTotal,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    @CacheEvict(cacheNames = {CACHE_ASSET_SUM, CACHE_CATEGORY_SUM, CACHE_MAIN_CATEGORY_SUM}, key = "#root.target.currentUserId()")
    public void deleteAsset(String id) {
        assetRepository.delete(findAssetOrThrow(id));
    }

    private Asset findAssetOrThrow(String id) {
        return assetRepository.findByIdAndUserId(id, currentUserId())
                .orElseThrow(() -> new AssetNotFoundException("Asset with ID " + id + " not found"));
    }
}