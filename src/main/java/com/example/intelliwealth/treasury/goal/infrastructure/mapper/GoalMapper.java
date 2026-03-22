package com.example.intelliwealth.treasury.goal.infrastructure.mapper;

import com.example.intelliwealth.treasury.goal.application.dto.GoalRequest;
import com.example.intelliwealth.treasury.goal.application.dto.GoalResponse;
import com.example.intelliwealth.treasury.goal.domain.model.Goal;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GoalMapper {

    // Entity to Response DTO

    GoalResponse toResponse(Goal goal);

    // Request DTO to Entity (for creation)
    Goal toEntity(GoalRequest request);

    // Update existing Entity from Request DTO
    // null values in the request will NOT overwrite existing values in the entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(@MappingTarget Goal goal, GoalRequest request);
}