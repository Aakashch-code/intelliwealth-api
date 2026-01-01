package com.example.intelliwealth.protection.insurance;

import org.springframework.stereotype.Component;

@Component
public class InsuranceMapper {

    public Insurance toEntity(InsuranceRequestDTO dto) {
        return Insurance.builder()
                .name(dto.getName())
                .provider(dto.getProvider())
                .category(dto.getCategory())
                .mainCategory(dto.getMainCategory())
                .premiumAmount(dto.getPremiumAmount())
                .coverageAmount(dto.getCoverageAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .attributes(dto.getAttributes())
                .build();
    }

    public InsuranceResponseDTO toDTO(Insurance insurance) {
        return InsuranceResponseDTO.builder()
                .id(insurance.getId())
                .name(insurance.getName())
                .provider(insurance.getProvider())
                .category(insurance.getCategory())
                .mainCategory(insurance.getMainCategory())
                .premiumAmount(insurance.getPremiumAmount())
                .coverageAmount(insurance.getCoverageAmount())
                .startDate(insurance.getStartDate())
                .endDate(insurance.getEndDate())
                .attributes(insurance.getAttributes())
                .build();
    }
}
