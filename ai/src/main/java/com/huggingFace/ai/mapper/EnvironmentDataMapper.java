package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.EnvironmentData;
import com.huggingFace.ai.dto.EnvironmentDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnvironmentDataMapper {

    @Mapping(source = "deviceIdentifier", target = "deviceIdentifier")
    @Mapping(source = "temperature", target = "temperature")
    @Mapping(source = "humidity", target = "humidity")
    @Mapping(source = "airQuality", target = "airQuality")


    EnvironmentDataDTO toDTO(EnvironmentData environmentData);

    @Mapping(source = "deviceIdentifier", target = "deviceIdentifier")
    @Mapping(source = "temperature", target = "temperature")
    @Mapping(source = "humidity", target = "humidity")
    @Mapping(source = "airQuality", target = "airQuality")
    EnvironmentData toEntity(EnvironmentDataDTO environmentDataDTO);
}