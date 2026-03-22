package com.example.intelliwealth.treasury.budget.infrastructure.mapper;

import com.example.intelliwealth.treasury.budget.application.dto.BudgetRequest;
import com.example.intelliwealth.treasury.budget.application.dto.BudgetResponse;
import com.example.intelliwealth.treasury.budget.domain.model.Budget;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    // Helper methods to access domain logic during mapping
    @Mapping(target = "remainingAmount", expression = "java(budget.getRemainingAmount())")
    @Mapping(target = "status", expression = "java(budget.getStatus())")
    @Mapping(target = "mode", expression = "java(budget.getMode())")
    BudgetResponse toResponseDTO(Budget budget);

    List<BudgetResponse> toResponseDTOList(List<Budget> budgets);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Budget toEntity(BudgetRequest requestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget Budget entity, BudgetRequest requestDTO);
}