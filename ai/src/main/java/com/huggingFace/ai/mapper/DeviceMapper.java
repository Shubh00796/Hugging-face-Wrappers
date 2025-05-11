package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.Device;
import com.huggingFace.ai.dto.DeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    DeviceDTO toDTO(Device device);

    @Mapping(target = "id", ignore = true)
    Device toEntity(DeviceDTO deviceDTO);
}