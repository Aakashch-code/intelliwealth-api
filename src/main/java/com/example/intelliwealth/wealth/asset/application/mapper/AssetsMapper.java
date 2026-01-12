package com.example.intelliwealth.wealth.asset.application.mapper;

import com.example.intelliwealth.wealth.asset.domain.model.Asset;
import com.example.intelliwealth.wealth.asset.api.dto.AssetsRequestDTO;
import com.example.intelliwealth.wealth.asset.api.dto.AssetsResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetsMapper {

    @Mapping(target = "id", ignore = true)
    Asset toEntity(AssetsRequestDTO dto);

    AssetsResponseDTO toDto(Asset asset);
}
