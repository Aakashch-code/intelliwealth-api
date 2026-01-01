package com.example.intelliwealth.core.subscription;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionRequestDTO dto);
    SubscriptionResponseDTO toResponse(Subscription entity);
}
