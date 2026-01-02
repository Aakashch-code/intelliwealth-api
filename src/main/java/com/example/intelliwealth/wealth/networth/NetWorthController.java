package com.example.intelliwealth.wealth.networth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/networth")
@RequiredArgsConstructor
@Tag(name = "Net Worth", description = "APIs for calculating user net worth")
@CrossOrigin(origins = "http://localhost:5173")
public class NetWorthController {

    private final NetWorthService netWorthService;

    @Operation(
            summary = "Get Net Worth",
            description = "Calculates total assets, total debts and net worth"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Net worth calculated successfully",
            content = @Content(schema = @Schema(implementation = NetWorthResponseDTO.class))
    )
    @GetMapping
    public NetWorthResponseDTO getNetWorth() {
        return netWorthService.calculateNetWorth();
    }
}
