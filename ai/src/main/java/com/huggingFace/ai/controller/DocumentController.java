package com.huggingFace.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huggingFace.ai.dto.request.DocumentRequest;
import com.huggingFace.ai.dto.response.DocumentResponse;
import com.huggingFace.ai.service.DocumentService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Slf4j
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<DocumentResponse>> uploadDocument(
            @RequestPart("requestJson") String requestJson,
            @RequestPart("file") MultipartFile file) {
        try {
            DocumentRequest request = new ObjectMapper().readValue(requestJson, DocumentRequest.class);
            log.info("Parsed JSON: {}", request);
            return documentService.uploadDocument(request, file)
                    .thenApply(ResponseEntity::ok);
        } catch (Exception e) {
            log.error("Error parsing JSON", e);
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<DocumentResponse>> getDocument(@PathVariable Long id) {
        return documentService.getDocument(id)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<DocumentResponse>>> getAllDocuments() {
        return documentService.getAllDocuments()
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/user/{uploadedBy}")
    public CompletableFuture<ResponseEntity<List<DocumentResponse>>> getDocumentsByUser(@PathVariable String uploadedBy) {
        return documentService.getDocumentsByUser(uploadedBy)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<DocumentResponse>> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentRequest request) {
        return documentService.updateDocument(id, request)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteDocument(@PathVariable Long id) {
        return documentService.deleteDocument(id)
                .thenApply(response -> ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}/extract-text")
    public CompletableFuture<ResponseEntity<String>> extractText(@PathVariable Long id) {
        return documentService.extractText(id)
                .thenApply(ResponseEntity::ok);
    }
}
