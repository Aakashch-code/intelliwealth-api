package com.example.intelliwealth.mapper.transactions;

import com.example.intelliwealth.dto.transactions.TransactionDTO;
import com.example.intelliwealth.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDTO toDTO(Transaction entity);

    Transaction toEntity(TransactionDTO dto);
}
