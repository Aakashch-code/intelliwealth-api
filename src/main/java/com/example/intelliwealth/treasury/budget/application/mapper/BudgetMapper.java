package com.example.intelliwealth.treasury.budget.application.mapper;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequestDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    // Entity to Response DTO
    BudgetResponseDTO toResponseDTO(Budget budget);

    List<BudgetResponseDTO> toResponseDTOList(List<Budget> budgets);

    // Request DTO to Entity
    Budget toEntity(BudgetRequestDTO requestDTO);

    // Update existing Entity from Request DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(@MappingTarget Budget entity, BudgetRequestDTO requestDTO);
}