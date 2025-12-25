package com.example.intelliwealth.mapper.subscription;

import com.example.intelliwealth.dto.subscription.SubscriptionRequestDTO;
import com.example.intelliwealth.dto.subscription.SubscriptionResponseDTO;
import com.example.intelliwealth.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionRequestDTO dto);
    SubscriptionResponseDTO toResponse(Subscription entity);
}
