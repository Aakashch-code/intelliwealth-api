package com.example.intelliwealth.wealth.debt.api;

import com.example.intelliwealth.wealth.debt.application.DebtService;
import com.example.intelliwealth.wealth.debt.application.dto.DebtRequestDTO;
import com.example.intelliwealth.wealth.debt.application.dto.DebtResponseDTO;
import com.example.intelliwealth.wealth.debt.application.dto.DebtStatsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
@Tag(name = "Debt Management", description = "APIs for managing user debts")
public class DebtController {

    private final DebtService service;

    @Operation(summary = "Get all debts")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public List<DebtResponseDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Get debt by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success"), @ApiResponse(responseCode = "404", description = "Not found")})
    @GetMapping("/{id}")
    public DebtResponseDTO getById(@PathVariable String id) {
        return service.getById(id);
    }

    @Operation(summary = "Create debt")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(schema = @Schema(implementation = DebtResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<DebtResponseDTO> create(@RequestBody DebtRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Update debt")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PutMapping("/{id}")
    public DebtResponseDTO update(@PathVariable String id, @RequestBody DebtRequestDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete debt")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @Operation(summary = "Get debt statistics")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/stats")
    public DebtStatsDTO stats() {
        return service.debtAmountSummary();
    }
}
