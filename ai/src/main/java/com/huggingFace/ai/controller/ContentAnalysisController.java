package com.huggingFace.ai.controller;

import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import com.huggingFace.ai.dto.response.ContentAnalysisResponse;
import com.huggingFace.ai.service.ContentAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class ContentAnalysisController {

    private final ContentAnalysisService contentAnalysisService;

    @PostMapping(value = "/{projectId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ContentAnalysisResponse> createAnalysis(@PathVariable("projectId") UUID projectId,
                                                        @Valid @RequestBody ContentAnalysisRequest request) {
        return contentAnalysisService.createAnalysis(request, projectId);
    }

    @GetMapping("/{id}")
    public ContentAnalysisResponse getAnalysis(@PathVariable("id") UUID id) {
        return contentAnalysisService.getAnalysis(id);
    }

    @GetMapping("/project/{projectId}")
    public List<ContentAnalysisResponse> getAnalysesByProject(@PathVariable("projectId") UUID projectId) {
        return contentAnalysisService.getAnalysesByProject(projectId);
    }
}
