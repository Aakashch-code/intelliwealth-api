package com.example.intelliwealth.wealth.debt;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DebtMapper {

    @Mapping(target = "id", ignore = true)
    Debt toEntity(DebtRequestDTO dto);

    DebtResponseDTO toDto(Debt debt);
}
