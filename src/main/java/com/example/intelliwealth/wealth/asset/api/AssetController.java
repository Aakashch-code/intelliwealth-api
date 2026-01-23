package com.example.intelliwealth.wealth.asset.api;

import com.example.intelliwealth.wealth.asset.application.AssetService;
import com.example.intelliwealth.wealth.asset.application.dto.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.application.dto.AssetsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Asset Management", description = "Operations related to managing financial assets")
public class AssetController {

    private final AssetService service;

    @GetMapping
    @Operation(summary = "Fetch all assets")
    public ResponseEntity<List<AssetsResponseDTO>> getAllAssets() {
        return ResponseEntity.ok(service.getAllAssets());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch asset by ID")
    public ResponseEntity<AssetsResponseDTO> getAssetById(@PathVariable String id) {
        return ResponseEntity.ok(service.getAssetById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new asset")
    public ResponseEntity<AssetsResponseDTO> postAsset(@Valid @RequestBody AssetsRequestDTO asset) {
        return new ResponseEntity<>(service.createAsset(asset), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update asset")
    public ResponseEntity<AssetsResponseDTO> updateAsset(
            @PathVariable String id,
            @Valid @RequestBody AssetsRequestDTO asset) {
        return ResponseEntity.ok(service.modifyAsset(asset, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete asset")
    public ResponseEntity<Void> deleteAsset(@PathVariable String id) {
        service.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-value")
    @Operation(summary = "Calculate total portfolio value")
    public ResponseEntity<BigDecimal> allAssetsAmount() {
        return ResponseEntity.ok(service.allAssetsAmount());
    }
}