package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import com.huggingFace.ai.dto.response.ContentAnalysisResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface HuggingFaceService {
    Mono<String> extractTextFromDocument(String documentContent);
    Mono<String> analyzeText(String text, String modelId);
    Mono<String> generateSummary(String text);
    Mono<String> extractKeyInsights(String text);
    Mono<ContentAnalysisResponse> analyzeContent(ContentAnalysisRequest request);

}
