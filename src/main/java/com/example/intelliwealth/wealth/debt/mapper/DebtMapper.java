package com.example.intelliwealth.wealth.debt.mapper;

import com.example.intelliwealth.wealth.debt.dto.DebtRequestDTO;
import com.example.intelliwealth.wealth.debt.dto.DebtResponseDTO;
import com.example.intelliwealth.wealth.debt.domain.Debt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DebtMapper {

    @Mapping(target = "id", ignore = true)
    Debt toEntity(DebtRequestDTO dto);

    DebtResponseDTO toDto(Debt debt);
}
