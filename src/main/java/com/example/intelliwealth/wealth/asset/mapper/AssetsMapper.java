package com.example.intelliwealth.wealth.asset.mapper;

import com.example.intelliwealth.wealth.asset.domain.Asset;
import com.example.intelliwealth.wealth.asset.dto.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.dto.AssetsResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetsMapper {

    @Mapping(target = "id", ignore = true)
    Asset toEntity(AssetsRequestDTO dto);

    AssetsResponseDTO toDto(Asset asset);
}
