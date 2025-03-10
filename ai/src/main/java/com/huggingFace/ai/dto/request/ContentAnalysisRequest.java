package com.huggingFace.ai.dto.request;

import com.huggingFace.ai.domain.enums.ContentAnalysisType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentAnalysisRequest {
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 10000, message = "Content must be less than 10000 characters")
    private String content;

    @NotNull(message = "Analysis type is required")
    private ContentAnalysisType type;

    private String model;

    private Map<String, Object> options;


}