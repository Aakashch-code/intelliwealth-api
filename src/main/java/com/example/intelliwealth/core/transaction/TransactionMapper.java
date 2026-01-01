package com.example.intelliwealth.core.transaction;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(TransactionRequestDTO dto);
    TransactionResponseDTO toResponse(Transaction entity);

}
