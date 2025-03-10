package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.ContentAnalysis;
import com.huggingFace.ai.domain.enums.ContentAnalysisStatus;
import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import com.huggingFace.ai.dto.response.ContentAnalysisResponse;
import com.huggingFace.ai.mapper.ContentAnalysisMapper;
import com.huggingFace.ai.reposiotryservices.ContentAnalysisRepositoryService;
import com.huggingFace.ai.service.ContentAnalysisService;
import com.huggingFace.ai.service.HuggingFaceService;
import com.huggingFace.ai.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentAnalysisServiceImpl implements ContentAnalysisService {
    private final ContentAnalysisRepositoryService repositoryService;
    private final ContentAnalysisMapper mapper;
    private final HuggingFaceService huggingFaceService;
    private final ValidationUtil validationUtil;

    @Override
    public Mono<ContentAnalysisResponse> createAnalysis(ContentAnalysisRequest request, UUID projectId) {
        log.info("Creating analysis for project: {}", projectId);
        validationUtil.validateAnalysisRequest(request);
        String contentHash = generateContentHash(request.getContent(), request.getType().name());

        // Wrap the blocking repository call and shift to boundedElastic scheduler.
        return Mono.fromCallable(() -> repositoryService.retriveAnalysisByContentHash(contentHash, request.getType().name()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable)
                .filter(analysis -> ContentAnalysisStatus.COMPLETED.equals(analysis.getStatus()))
                .next()
                .flatMap(existing -> {
                    log.info("Found existing completed analysis with ID: {}", existing.getId());
                    return Mono.just(mapper.toResponse(existing));
                })
                .switchIfEmpty(createNewAnalysis(request, projectId, contentHash));
    }

    private Mono<ContentAnalysisResponse> createNewAnalysis(ContentAnalysisRequest request, UUID projectId, String contentHash) {
        ContentAnalysis contentAnalysis = mapper.toEntity(request);
        contentAnalysis.setContentHash(contentHash);
        contentAnalysis.setCreatedAt(LocalDateTime.now());
        contentAnalysis.setProjectId(projectId);
        contentAnalysis.setStatus(ContentAnalysisStatus.PENDING);

        // Save the new analysis reactively.
        return repositoryService.saveContentAnalyisis(contentAnalysis)
                .doOnSuccess(saved -> log.info("New analysis created with ID: {}", saved.getId()))
                .flatMap(saved -> huggingFaceService.analyzeContent(request)
                        .map(result -> mapper.toResponse(saved)));
    }

    @Override
    public ContentAnalysisResponse getAnalysis(UUID id) {
        ContentAnalysis analysis = repositoryService.retriveAnalysisById(id);
        return mapper.toResponse(analysis);
    }

    @Override
    public List<ContentAnalysisResponse> getAnalysesByProject(UUID projectId) {
        // Retrieve analyses synchronously using the repository service and map each to a response.
        List<ContentAnalysis> analyses = repositoryService.retriveAnalysisByProjectId(projectId);
        return analyses.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    private String generateContentHash(String content, String type) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String contentToHash = content + type;
            byte[] hash = digest.digest(contentToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating content hash", e);
            return String.valueOf(content.hashCode());
        }
    }
}
