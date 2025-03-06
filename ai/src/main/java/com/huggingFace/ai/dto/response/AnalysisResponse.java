package com.huggingFace.ai.dto.response;

import com.huggingFace.ai.domain.enums.AnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {

    private Long id;
    private Long documentId;
    private String modelUsed;
    private String summary;
    private String keyInsights;
    private AnalysisStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
}