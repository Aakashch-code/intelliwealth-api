package com.example.intelliwealth.treasury.subscription.application.mapper;

import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionRequestDTO;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionResponseDTO;
import com.example.intelliwealth.treasury.subscription.domain.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionRequestDTO dto);
    SubscriptionResponseDTO toResponse(Subscription entity);
}
