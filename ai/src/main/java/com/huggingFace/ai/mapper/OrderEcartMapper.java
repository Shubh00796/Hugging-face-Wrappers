package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.Analysis;
import com.huggingFace.ai.domain.EcartOrder;
import com.huggingFace.ai.dto.EcartOrderDTO;
import com.huggingFace.ai.dto.request.AnalysisRequest;
import com.huggingFace.ai.dto.response.AnalysisResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderEcartMapper {

    @Mapping(target = "id", ignore = true)
    EcartOrderDTO toDTO(EcartOrder order);

    @Mapping(target = "id", ignore = true)
    EcartOrder toEntity(EcartOrderDTO orderDTO);

    @Mapping(target = "id", ignore = true)
    void updateFromDTO(@MappingTarget EcartOrder order, EcartOrderDTO orderDTO);


}
