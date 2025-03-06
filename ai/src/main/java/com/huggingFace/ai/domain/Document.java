package com.huggingFace.ai.domain;

import com.huggingFace.ai.domain.enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contentPath;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    private Long sizeInBytes;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    private String uploadedBy;
}
