package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.Analysis;
import com.huggingFace.ai.domain.enums.AnalysisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByDocumentId(Long documentId);
    List<Analysis> findByStatus(AnalysisStatus status);
    Optional<Analysis> findTopByDocumentIdOrderByRequestedAtDesc(Long documentId);
}