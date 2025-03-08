package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import com.huggingFace.ai.dto.response.ContentAnalysisResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ContentAnalysisService {
    Mono<ContentAnalysisResponse> createAnalysis(ContentAnalysisRequest request, UUID projectId);

    ContentAnalysisResponse getAnalysis(UUID id);

    List<ContentAnalysisResponse> getAnalysesByProject(UUID projectId);
}
