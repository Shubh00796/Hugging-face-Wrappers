package com.huggingFace.ai.reposiotryservices;

import com.huggingFace.ai.domain.Analysis;
import com.huggingFace.ai.domain.enums.AnalysisStatus;
import com.huggingFace.ai.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AnalysisReposiotryService {
    private final AnalysisRepository repository;


    public Analysis saveAnalysis(Analysis analysis) {
        return repository.save(analysis);
    }

    public Optional<Analysis> getAnalysis(Long id) {
        return repository.findById(id);
    }

    public List<Analysis> getAnalysesByDocument(Long documentId) {
        return repository.findByDocumentId(documentId);
    }

    public Optional<Analysis> getLatestAnalysisByDocument(Long documentId) {
        return repository.findTopByDocumentIdOrderByRequestedAtDesc(documentId);
    }

    public void deleteAnalysis(Long id) {
        repository.deleteById(id);
    }

    public List<Analysis> getAnalysesByStatus(AnalysisStatus status) {
        return repository.findByStatus(status);
    }

}
