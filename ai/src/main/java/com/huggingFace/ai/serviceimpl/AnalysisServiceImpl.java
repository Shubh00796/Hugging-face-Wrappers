package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.Analysis;
import com.huggingFace.ai.domain.enums.AnalysisStatus;
import com.huggingFace.ai.dto.request.AnalysisRequest;
import com.huggingFace.ai.dto.response.AnalysisResponse;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.mapper.AnalysisMapper;
import com.huggingFace.ai.reposiotryservices.AnalysisReposiotryService;
import com.huggingFace.ai.reposiotryservices.DocumentReposiotryService;
import com.huggingFace.ai.service.AnalysisService;
import com.huggingFace.ai.service.DocumentService;
import com.huggingFace.ai.service.HuggingFaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisReposiotryService analysisReposiotryService;
    private final DocumentReposiotryService documentReposiotryService;
    private final DocumentService documentService;
    private final HuggingFaceService huggingFaceService;
    private final AnalysisMapper mapper;
    private final ConcurrentHashMap<Long, ReentrantLock> analysisLock = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<AnalysisResponse> requestAnalysis(AnalysisRequest request) {
        log.info("Requesting analysis for document ID: {}", request.getDocumentId());

        documentReposiotryService.retriveDocumentById(request.getDocumentId());

        Analysis entity = mapper.toEntity(request);
        entity.setModelUsed(request.getModelId() != null ? request.getModelId() : "Default");
        entity.setStatus(AnalysisStatus.PROCESSING);

        Analysis savedAnalysis = analysisReposiotryService.saveAnalysis(entity);

        processAnalysis(savedAnalysis, request);

        AnalysisResponse response = mapper.toResponse(savedAnalysis);
        return CompletableFuture.completedFuture(response);
    }

        protected void processAnalysis(Analysis analysis, AnalysisRequest request) {
            ReentrantLock lock = analysisLock.computeIfAbsent(analysis.getId(), id -> new ReentrantLock());
            lock.lock();
            try {
                log.info("Processing analysis ID: {}", analysis.getId());

                updateStatus(analysis, AnalysisStatus.PROCESSING);

                String extractedText = extractDocumentText(request.getDocumentId());
                analysis.setExtractedText(extractedText);

                String analyzedContent = analyzeText(extractedText, request.getModelId());
                analysis.setAnalyzedContent(analyzedContent);

                if (request.getGenerateSummary()) {
                    String summary = generateSummary(extractedText);
                    analysis.setSummary(summary);
                }

                if (request.getExtractKeyInsights()) {
                    String keyInsights = extractKeyInsights(extractedText);
                    analysis.setKeyInsights(keyInsights);
                }

                analysis.setCompletedAt(LocalDateTime.now());
                updateStatus(analysis, AnalysisStatus.COMPLETED);

            } catch (Exception e) {
                log.error("Error processing analysis ID: {}", analysis.getId(), e);
                analysis.setErrorMessage(e.getMessage());
                updateStatus(analysis, AnalysisStatus.FAILED);
                throw new RuntimeException("Failed to process analysis", e);
            } finally {
                analysisReposiotryService.saveAnalysis(analysis);
                lock.unlock();
                analysisLock.remove(analysis.getId());
            }
        }
        private String generateSummary(String text) {
            try {
                return huggingFaceService.generateSummary(text).block();
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate summary", e);
            }
        }

        private String extractKeyInsights(String text) {
            try {
                return huggingFaceService.extractKeyInsights(text).block();
            } catch (Exception e) {
                throw new RuntimeException("Failed to extract key insights", e);
            }
        }


    private void updateStatus(Analysis analysis, AnalysisStatus status) {
        analysis.setStatus(status);
        analysisReposiotryService.saveAnalysis(analysis);
    }

    private String extractDocumentText(Long documentId) {
        try {
            return documentService.extractText(documentId).get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from document", e);
        }
    }

    private String analyzeText(String text, String modelId) {
        try {
            return huggingFaceService.analyzeText(text, modelId).block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze text", e);
        }
    }

    @Override
    public CompletableFuture<Void> deleteAnalysis(Long id) {
        ReentrantLock lock = analysisLock.computeIfAbsent(id, key -> new ReentrantLock());
        lock.lock();
        try {
            log.info("Deleting analysis with ID: {}", id);
            analysisReposiotryService.deleteAnalysis(id);
            return CompletableFuture.completedFuture(null);
        } finally {
            lock.unlock();
            analysisLock.remove(id);
        }
    }

    @Override
    public CompletableFuture<AnalysisResponse> getAnalysis(Long id) {
        return CompletableFuture.supplyAsync(() -> {
            Analysis analysis = analysisReposiotryService.getAnalysis(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Analysis not found with ID: " + id));
            return mapper.toResponse(analysis);
        });
    }

    @Override
    public CompletableFuture<List<AnalysisResponse>> getAnalysesByDocument(Long documentId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Analysis> analyses = analysisReposiotryService.getAnalysesByDocument(documentId);
            return analyses.stream()
                    .map(mapper::toResponse)
                    .toList();
        });
    }

    @Override
    public CompletableFuture<AnalysisResponse> getLatestAnalysisByDocument(Long documentId) {
        return CompletableFuture.supplyAsync(() -> {
            Analysis analysis = analysisReposiotryService.getLatestAnalysisByDocument(documentId)
                    .orElseThrow(() -> new ResourceNotFoundException("No analysis found for document ID: " + documentId));
            return mapper.toResponse(analysis);
        });
    }

}
