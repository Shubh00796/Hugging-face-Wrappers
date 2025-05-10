package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.PackageEntity;
import com.huggingFace.ai.domain.Tracking;
import com.huggingFace.ai.dto.PackageDTO;
import com.huggingFace.ai.dto.TrackingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PackageMapper {

    @Mapping(target = "id", ignore = true)
    PackageDTO toDTO(PackageEntity packageEntity);

    @Mapping(target = "id", ignore = true)
    PackageEntity toEntity(PackageDTO packageDTO);

    @Mapping(target = "id", ignore = true)
    void updateFromDTO(@MappingTarget PackageEntity packageEntity, PackageDTO packageDTO);


}
