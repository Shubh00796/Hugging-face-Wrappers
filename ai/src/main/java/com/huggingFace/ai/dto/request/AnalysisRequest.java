package com.huggingFace.ai.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

    @NotNull(message = "Document ID is required")
    private Long documentId;

    private String modelId;

    private Boolean generateSummary;

    private Boolean extractKeyInsights;
}