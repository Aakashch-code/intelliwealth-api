package com.example.intelliwealth.dto.assets;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Assets data object")
public class AssetsDTO {

    private String name;
    @Schema(
            description = "Assets Name",
            example = "Laptop,2BHK House...",
            allowableValues = {"String"}
    )
    private String type;
    @Schema(
            description = "Type"
    )
    private BigDecimal currentAmount;
    private LocalDate dateAccuried;


}
