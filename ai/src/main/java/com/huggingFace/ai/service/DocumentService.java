package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.request.DocumentRequest;
import com.huggingFace.ai.dto.response.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DocumentService {
    CompletableFuture<DocumentResponse> uploadDocument(DocumentRequest request, MultipartFile file);

    CompletableFuture<DocumentResponse> getDocument(Long id);

    CompletableFuture<List<DocumentResponse>> getAllDocuments();

    CompletableFuture<List<DocumentResponse>> getDocumentsByUser(String uploadedBy);

    CompletableFuture<DocumentResponse> updateDocument(Long id, DocumentRequest request);

    CompletableFuture<Void> deleteDocument(Long id);

    CompletableFuture<String> extractText(Long id);
}
