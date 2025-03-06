package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.request.AnalysisRequest;
import com.huggingFace.ai.dto.response.AnalysisResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AnalysisService {
    CompletableFuture<AnalysisResponse> requestAnalysis(AnalysisRequest request);
    CompletableFuture<AnalysisResponse> getAnalysis(Long id);
    CompletableFuture<List<AnalysisResponse>> getAnalysesByDocument(Long documentId);
    CompletableFuture<AnalysisResponse> getLatestAnalysisByDocument(Long documentId);
    CompletableFuture<Void> deleteAnalysis(Long id);
}
