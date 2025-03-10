package com.huggingFace.ai.reposiotryservices;

import com.huggingFace.ai.domain.ContentAnalysis;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.repository.ContentAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContentAnalysisRepositoryService {
    private final ContentAnalysisRepository contentAnalysisRepository;

    // Wrap the blocking save operation in a Mono.
    public Mono<ContentAnalysis> saveContentAnalyisis(ContentAnalysis contentAnalysis) {
        return Mono.fromCallable(() -> contentAnalysisRepository.save(contentAnalysis))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public ContentAnalysis retriveAnalysisById(UUID id) {
        return contentAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id with given analysis not found: " + id));
    }

    public List<ContentAnalysis> retriveAnalysisByProjectId(UUID projectId) {
        return contentAnalysisRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }

    public List<ContentAnalysis> retriveAnalysisByContentHash(String contentHash, String type) {
        return contentAnalysisRepository.findByContentHashAndType(contentHash, type);
    }
}
