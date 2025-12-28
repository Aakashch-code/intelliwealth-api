package com.example.intelliwealth.wealth.asset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetsMapper {

    @Mapping(target = "id", ignore = true)
    Asset toEntity(AssetsRequestDTO dto);

    AssetsResponseDTO toDto(Asset asset);
}
