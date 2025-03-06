package com.huggingFace.ai.service;

import reactor.core.publisher.Mono;

public interface HuggingFaceService {
    Mono<String> extractTextFromDocument(String documentContent);
    Mono<String> analyzeText(String text, String modelId);
    Mono<String> generateSummary(String text);
    Mono<String> extractKeyInsights(String text);
}
