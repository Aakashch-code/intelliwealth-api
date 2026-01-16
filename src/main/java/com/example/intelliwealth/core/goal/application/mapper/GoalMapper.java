package com.example.intelliwealth.core.goal.application.mapper;

import com.example.intelliwealth.core.goal.application.dto.GoalRequestDTO;
import com.example.intelliwealth.core.goal.application.dto.GoalResponseDTO;
import com.example.intelliwealth.core.goal.domain.model.Goal;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GoalMapper {

    // Entity to Response DTO
    GoalResponseDTO toResponse(Goal goal);

    // Request DTO to Entity (for creation)
    Goal toEntity(GoalRequestDTO request);

    // Update existing Entity from Request DTO
    // null values in the request will NOT overwrite existing values in the entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(@MappingTarget Goal goal, GoalRequestDTO request);
}