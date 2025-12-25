package com.example.intelliwealth.mapper.transactions;

import com.example.intelliwealth.dto.transactions.TransactionRequestDTO;
import com.example.intelliwealth.dto.transactions.TransactionResponseDTO;
import com.example.intelliwealth.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(TransactionRequestDTO dto);
    TransactionResponseDTO toResponse(Transaction entity);

}
