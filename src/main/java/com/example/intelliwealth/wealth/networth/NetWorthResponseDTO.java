package com.example.intelliwealth.wealth.networth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Net Worth Summary Response")
public class NetWorthResponseDTO {

    @Schema(example = "1500000")
    private BigDecimal totalAssets;

    @Schema(example = "450000")
    private BigDecimal totalDebts;

    @Schema(example = "1050000")
    private BigDecimal netWorth;
}
