package com.huggingFace.ai.dto.request;

import com.huggingFace.ai.domain.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

    @NotBlank(message = "Document name is required")
    private String name;

    @NotNull(message = "Document type is required")
    private DocumentType type;

    private String uploadedBy;
}