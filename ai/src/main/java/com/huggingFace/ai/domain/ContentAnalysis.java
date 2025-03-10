package com.huggingFace.ai.domain;

import com.huggingFace.ai.domain.enums.ContentAnalysisStatus;
import com.huggingFace.ai.domain.enums.ContentAnalysisType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(name = "content_hash", nullable = false)
    private String contentHash;

    @Column(name = "input_content", length = 10000)
    private String inputContent;

    @Column(name = "result_content", columnDefinition = "TEXT")
    private String resultContent;

    @Column(name = "model_used")
    private String modelUsed;

    @Column(name = "processing_time")
    private Long processingTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "project_id")
    private UUID projectId;

    @Column(name = "statusRype")
    @Enumerated(EnumType.STRING)
    private ContentAnalysisType statusType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ContentAnalysisStatus status;

    @Column(name = "error_message")
    private String errorMessage;


}