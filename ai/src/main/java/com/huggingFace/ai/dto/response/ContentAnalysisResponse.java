package com.huggingFace.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentAnalysisResponse {
    private UUID id;
    private String type;
    private Map<String, Object> result;
    private String modelUsed;
    private Long processingTime;
    private LocalDateTime createdAt;
    private String status;
    private String errorMessage;
}
