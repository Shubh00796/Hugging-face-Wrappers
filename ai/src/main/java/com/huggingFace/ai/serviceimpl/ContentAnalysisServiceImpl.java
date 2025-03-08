package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import com.huggingFace.ai.dto.response.ContentAnalysisResponse;
import com.huggingFace.ai.service.ContentAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentAnalysisServiceImpl implements ContentAnalysisService {
    @Override
    public Mono<ContentAnalysisResponse> createAnalysis(ContentAnalysisRequest request, UUID projectId) {
        return null;
    }

    @Override
    public ContentAnalysisResponse getAnalysis(UUID id) {
        return null;
    }

    @Override
    public List<ContentAnalysisResponse> getAnalysesByProject(UUID projectId) {
        return List.of();
    }
}
