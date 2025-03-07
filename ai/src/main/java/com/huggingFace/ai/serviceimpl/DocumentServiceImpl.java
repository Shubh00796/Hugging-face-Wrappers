package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.Document;
import com.huggingFace.ai.dto.request.DocumentRequest;
import com.huggingFace.ai.dto.response.DocumentResponse;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.mapper.DocumentMapper;
import com.huggingFace.ai.reposiotryservices.DocumentReposiotryService;
import com.huggingFace.ai.service.DocumentService;
import com.huggingFace.ai.service.HuggingFaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentMapper documentMapper;
    private final DocumentReposiotryService repositoryService;
    private final HuggingFaceService huggingFaceService;

    // A map to store locks per document (to handle concurrent updates)
    private final ConcurrentHashMap<Long, ReentrantLock> docLocks = new ConcurrentHashMap<>();

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<DocumentResponse> uploadDocument(DocumentRequest request, MultipartFile file) {
        log.info("Uploading document: {}", request.getName());
        validateFile(file);
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            Document document = documentMapper.toEntity(request);
            document.setSizeInBytes(file.getSize());
            document.setContentPath(filePath.toString());
            Document savedDocument = repositoryService.saveDocument(document);

            return CompletableFuture.completedFuture(documentMapper.toResponse(savedDocument));
        } catch (IOException e) {
            log.error("Failed to store file in directory {}: {}", uploadDir, e.getMessage(), e);
            throw new ResourceNotFoundException("Failed to store file: " + e.getMessage());
        }
    }
    @Override
    @Async("taskExecutor")
    public CompletableFuture<DocumentResponse> getDocument(Long id) {
        log.info("Fetching document with ID: {}", id);
        Document document = repositoryService.retriveDocumentById(id);
        return CompletableFuture.completedFuture(documentMapper.toResponse(document));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<DocumentResponse>> getAllDocuments() {
        log.info("Fetching all documents");
        List<Document> documents = repositoryService.retriveAllDocuments();
        List<DocumentResponse> responses = documents.stream()
                .map(documentMapper::toResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<DocumentResponse>> getDocumentsByUser(String uploadedBy) {
        log.info("Fetching documents uploaded by: {}", uploadedBy);
        List<Document> documents = repositoryService.retriveAllDocumentsByUsers(uploadedBy);
        List<DocumentResponse> responses = documents.stream()
                .map(documentMapper::toResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(responses);
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<DocumentResponse> updateDocument(Long documentId, DocumentRequest request) {
        if (documentId == null) {
            throw new IllegalArgumentException("Document id cannot be null");
        }
        ReentrantLock lock = docLocks.computeIfAbsent(documentId, id -> new ReentrantLock());
        try {
            if (!lock.tryLock(5, TimeUnit.MILLISECONDS)) {
                throw new ConcurrentModificationException("Unable to acquire lock for updating document with id: " + documentId);
            }
            Document document = repositoryService.retriveDocumentById(documentId);
            documentMapper.updateEntityFromRequest(request, document);
            Document savedDocument = repositoryService.saveDocument(document);
            return CompletableFuture.completedFuture(documentMapper.toResponse(savedDocument));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while updating document", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> deleteDocument(Long id) {
        Document document = repositoryService.retriveDocumentById(id);
        try {
            Files.deleteIfExists(Paths.get(document.getContentPath()));
        } catch (IOException e) {
            log.warn("Failed to delete file {}: {}", document.getContentPath(), e.getMessage());
        }
        repositoryService.deleteDocument(id);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> extractText(Long id) {
        log.info("Extracting text from document with ID: {}", id);
        Document document = repositoryService.retriveDocumentById(id);
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(document.getContentPath())));
            return huggingFaceService.extractTextFromDocument(fileContent).toFuture();
        } catch (IOException e) {
            log.error("Failed to read file {}: {}", document.getContentPath(), e.getMessage(), e);
            throw new ResourceNotFoundException("Failed to read file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB
            throw new IllegalArgumentException("File is too large (max 10 MB)");
        }
    }
}
