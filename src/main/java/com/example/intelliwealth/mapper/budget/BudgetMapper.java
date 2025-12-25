package com.example.intelliwealth.mapper.budget;

import com.example.intelliwealth.dto.budget.BudgetRequestDTO;
import com.example.intelliwealth.dto.budget.BudgetResponseDTO;
import com.example.intelliwealth.model.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    // Entity to Response DTO
    BudgetResponseDTO toResponseDTO(Budget budget);

    List<BudgetResponseDTO> toResponseDTOList(List<Budget> budgets);

    // Request DTO to Entity
    Budget toEntity(BudgetRequestDTO requestDTO);

    // Update existing Entity from Request DTO
    void updateEntityFromRequest(BudgetRequestDTO requestDTO, @MappingTarget Budget entity);
}