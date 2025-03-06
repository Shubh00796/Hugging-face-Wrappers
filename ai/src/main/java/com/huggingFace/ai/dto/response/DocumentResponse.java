package com.huggingFace.ai.dto.response;

import com.huggingFace.ai.domain.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private Long id;
    private String name;
    private DocumentType type;
    private Long sizeInBytes;
    private LocalDateTime uploadedAt;
    private String uploadedBy;
}