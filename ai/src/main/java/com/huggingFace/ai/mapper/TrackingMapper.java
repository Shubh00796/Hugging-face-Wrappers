package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.EcartOrder;
import com.huggingFace.ai.domain.Tracking;
import com.huggingFace.ai.dto.EcartOrderDTO;
import com.huggingFace.ai.dto.TrackingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrackingMapper {

    @Mapping(target = "id", ignore = true)
    TrackingDTO toDTO(Tracking tracking);

    @Mapping(target = "id", ignore = true)
    Tracking toEntity(TrackingDTO trackingDTO);

    @Mapping(target = "id", ignore = true)
    void updateFromDTO(@MappingTarget Tracking tracking, TrackingDTO trackingDTO);


}
