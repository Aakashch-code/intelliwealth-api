package com.example.intelliwealth.wealth.asset.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetPriority {
    PRIMARY("Primary"),
    SECONDARY("Secondary");

    private final String label;




}
