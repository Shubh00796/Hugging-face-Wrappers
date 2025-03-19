package com.huggingFace.ai.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huggingFace.ai.domain.ContentAnalysis;
import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import com.huggingFace.ai.dto.response.ContentAnalysisResponse;
import com.huggingFace.ai.domain.enums.ContentAnalysisType;
import org.mapstruct.*;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ContentAnalysisMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contentHash", ignore = true)
    @Mapping(target = "inputContent", source = "content")
    @Mapping(target = "resultContent", ignore = true)
    @Mapping(target = "modelUsed", source = "model")
    @Mapping(target = "processingTime", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "analysisTypeToString")
    ContentAnalysis toEntity(ContentAnalysisRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "result", source = "resultContent", qualifiedByName = "jsonToListMap")
    @Mapping(target = "modelUsed", source = "modelUsed")
    @Mapping(target = "processingTime", source = "processingTime")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "errorMessage", source = "errorMessage")
    ContentAnalysisResponse toResponse(ContentAnalysis contentAnalysis);

    @Named("analysisTypeToString")
    default String analysisTypeToString(ContentAnalysisType type) {
        return type != null ? type.name() : null;
    }

    @Named("jsonToListMap")
    default List<Map<String, Object>> jsonToListMap(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(json, List.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON content", e);
        }
    }
}
