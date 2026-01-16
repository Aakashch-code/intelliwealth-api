package com.example.intelliwealth.core.transaction.application.mapper;

import com.example.intelliwealth.core.transaction.application.dto.TransactionRequest;
import com.example.intelliwealth.core.transaction.application.dto.TransactionResponse;
import com.example.intelliwealth.core.transaction.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(TransactionRequest dto);

    TransactionResponse toResponse(Transaction entity);

    void updateEntityFromRequest(TransactionRequest dto, @MappingTarget Transaction entity);
}