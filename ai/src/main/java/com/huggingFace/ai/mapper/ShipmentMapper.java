package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.Tracking;
import com.huggingFace.ai.dto.ShipmentDTO;
import com.huggingFace.ai.dto.TrackingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    ShipmentDTO toDTO(Tracking tracking);

    @Mapping(target = "id", ignore = true)
    Tracking toEntity(ShipmentDTO shipmentDTO);

    @Mapping(target = "id", ignore = true)
    void updateFromDTO(@MappingTarget Tracking tracking, ShipmentDTO shipmentDTO);


}
