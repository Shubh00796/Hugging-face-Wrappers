package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.ContentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentAnalysisRepository extends JpaRepository<ContentAnalysis, UUID> {
    List<ContentAnalysis> findByProjectIdOrderByCreatedAtDesc(UUID projectId);

    List<ContentAnalysis> findByContentHashAndType(String contentHash, String type);
}