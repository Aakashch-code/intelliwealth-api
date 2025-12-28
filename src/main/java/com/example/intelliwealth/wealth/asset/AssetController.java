package com.example.intelliwealth.wealth.asset;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@CrossOrigin("http://localhost:5173") // Your React Frontend Port
@RequiredArgsConstructor
@Tag(name = "Asset Management", description = "Operations related to managing financial assets in the IntelliWealth portfolio")
public class AssetController {

    private final AssetService service;

    @Operation(summary = "Fetch all assets", description = "Returns a list of all assets currently stored in the database.")
    @GetMapping
    public ResponseEntity<List<AssetsResponseDTO>> getAllAssets() {
        return ResponseEntity.ok(service.getAllAssets());
    }

    @Operation(summary = "Calculate total portfolio value", description = "Sums the 'currentValue' of all assets.")
    @GetMapping("/total-value")
    public ResponseEntity<BigDecimal> allAssetsAmount() {
        return ResponseEntity.ok(service.allAssetsAmount());
    }

    @Operation(summary = "Fetch asset by ID", description = "Returns a specific asset based on its MongoDB Object ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved asset",
                    content = @Content(schema = @Schema(implementation = Asset.class))),
            @ApiResponse(responseCode = "404", description = "Asset not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(
            @Parameter(description = "The MongoDB ObjectId", example = "6564e2076044321748d56c42")
            @PathVariable String id) {
        return ResponseEntity.ok(service.getAssetById(id));
    }

    @Operation(summary = "Create a new asset", description = "Creates a new asset record. ID is auto-generated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asset created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Asset> postAsset(@Valid @RequestBody AssetsRequestDTO asset) {
        return new ResponseEntity<>(service.createAsset(asset), HttpStatus.CREATED);
    }

    @Operation(summary = "Update asset", description = "Updates an existing asset's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asset updated successfully"),
            @ApiResponse(responseCode = "404", description = "Asset ID not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(
            @Parameter(description = "The MongoDB ObjectId of the asset to update") @PathVariable String id,
            @Valid @RequestBody AssetsRequestDTO asset) {
        return ResponseEntity.ok(service.modifyAsset(asset, id));
    }

    @Operation(summary = "Delete asset", description = "Permanently removes the asset from the portfolio.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(
            @Parameter(description = "The MongoDB ObjectId to delete") @PathVariable String id) {
        service.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}