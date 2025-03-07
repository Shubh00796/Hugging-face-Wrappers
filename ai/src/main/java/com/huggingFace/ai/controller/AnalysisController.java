package com.huggingFace.ai.controller;

import com.huggingFace.ai.dto.request.AnalysisRequest;
import com.huggingFace.ai.dto.response.AnalysisResponse;
import com.huggingFace.ai.service.AnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/analyses")
@RequiredArgsConstructor
@Slf4j
public class AnalysisController {

    private final AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<AnalysisResponse> requestAnalysis(@Valid @RequestBody AnalysisRequest request) {
        log.info("Received request to analyze document ID: {}", request.getDocumentId());
        try {
            AnalysisResponse response = analysisService.requestAnalysis(request).get();
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while requesting analysis", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to request analysis", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisResponse> getAnalysis(@PathVariable Long id) {
        log.info("Received request to get analysis with ID: {}", id);
        try {
            AnalysisResponse response = analysisService.getAnalysis(id).get();
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while getting analysis", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to get analysis", e);
        }
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<AnalysisResponse>> getAnalysesByDocument(@PathVariable Long documentId) {
        log.info("Received request to get analyses for document ID: {}", documentId);
        try {
            List<AnalysisResponse> responses = analysisService.getAnalysesByDocument(documentId).get();
            return ResponseEntity.ok(responses);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while getting analyses by document", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to get analyses by document", e);
        }
    }

    @GetMapping("/document/{documentId}/latest")
    public ResponseEntity<AnalysisResponse> getLatestAnalysisByDocument(@PathVariable Long documentId) {
        log.info("Received request to get latest analysis for document ID: {}", documentId);
        try {
            AnalysisResponse response = analysisService.getLatestAnalysisByDocument(documentId).get();
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while getting latest analysis", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to get latest analysis", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        log.info("Received request to delete analysis with ID: {}", id);
        try {
            analysisService.deleteAnalysis(id).get();
            return ResponseEntity.noContent().build();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while deleting analysis", e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to delete analysis", e);
        }
    }
}
