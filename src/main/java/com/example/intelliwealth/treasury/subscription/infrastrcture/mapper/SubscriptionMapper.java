package com.example.intelliwealth.treasury.subscription.infrastrcture.mapper;

import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionRequest;
import com.example.intelliwealth.treasury.subscription.application.dto.SubscriptionResponse;
import com.example.intelliwealth.treasury.subscription.domain.model.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toEntity(SubscriptionRequest dto);
    SubscriptionResponse toResponse(Subscription entity);
}
