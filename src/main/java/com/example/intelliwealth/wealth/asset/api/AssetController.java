package com.example.intelliwealth.wealth.asset.api;

import com.example.intelliwealth.authentication.application.service.SecuredService;
import com.example.intelliwealth.wealth.asset.application.service.AssetService;
import com.example.intelliwealth.wealth.asset.application.dto.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.application.dto.AssetsResponseDTO;
import com.example.intelliwealth.wealth.asset.domain.model.AssetCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
@Tag(name = "Asset Management", description = "Manage financial assets")
public class AssetController extends SecuredService {

    private final AssetService service;

    // ------------------- CRUD -------------------

    @GetMapping
    @Operation(summary = "Get all assets (paginated)")
    public ResponseEntity<Page<AssetsResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAllAssets(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by ID")
    public ResponseEntity<AssetsResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getAssetById(id));
    }

    @PostMapping
    @Operation(summary = "Create new asset")
    public ResponseEntity<AssetsResponseDTO> create(@Valid @RequestBody AssetsRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createAsset(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing asset")
    public ResponseEntity<AssetsResponseDTO> update(
            @PathVariable String id,
            @Valid @RequestBody AssetsRequestDTO request) {
        return ResponseEntity.ok(service.modifyAsset(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete asset by ID")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    // ------------------- ANALYTICS -------------------

    @GetMapping("/analytics/category-totals")
    @Operation(summary = "Get total value grouped by category")
    public ResponseEntity<Map<String, BigDecimal>> getCategoryTotals() {
        return ResponseEntity.ok(service.getTotalValueByCategory());
    }

    @GetMapping("/analytics/main-category-totals")
    @Operation(summary = "Get total value grouped by main category")
    public ResponseEntity<Map<String, BigDecimal>> getMainCategoryTotals() {
        return ResponseEntity.ok(service.getTotalValueByMainCategory());
    }

    @GetMapping("/analytics/total-value")
    @Operation(summary = "Get total portfolio value")
    public ResponseEntity<BigDecimal> getTotalValue() {
        return ResponseEntity.ok(service.getTotalAssetValue());
    }
}