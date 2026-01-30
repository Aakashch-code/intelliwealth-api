package com.example.intelliwealth.treasury.budget.application.mapper;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequestDTO;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponseDTO;
import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    // Helper methods to access domain logic during mapping
    @Mapping(target = "remainingAmount", expression = "java(budget.getRemainingAmount())")
    @Mapping(target = "status", expression = "java(budget.getStatus())")
    BudgetResponseDTO toResponseDTO(Budget budget);

    List<BudgetResponseDTO> toResponseDTOList(List<Budget> budgets);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Budget toEntity(BudgetRequestDTO requestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget Budget entity, BudgetRequestDTO requestDTO);
}