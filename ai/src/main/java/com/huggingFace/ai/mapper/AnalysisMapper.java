package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.Analysis;
import com.huggingFace.ai.dto.request.AnalysisRequest;
import com.huggingFace.ai.dto.response.AnalysisResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface AnalysisMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "extractedText", ignore = true)
    @Mapping(target = "analyzedContent", ignore = true)
    @Mapping(target = "summary", ignore = true)
    @Mapping(target = "keyInsights", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "requestedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    Analysis toEntity(AnalysisRequest request);

    AnalysisResponse toResponse(Analysis analysis);
}