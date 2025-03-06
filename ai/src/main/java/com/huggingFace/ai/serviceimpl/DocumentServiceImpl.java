package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.Document;
import com.huggingFace.ai.dto.request.DocumentRequest;
import com.huggingFace.ai.dto.response.DocumentResponse;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.mapper.DocumentMapper;
import com.huggingFace.ai.repository.DocumentReposiotryService;
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
    private final DocumentReposiotryService reposiotryService;
    private final HuggingFaceService huggingFaceService;
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
            Document savedDocument = reposiotryService.saveDocument(document);
            return CompletableFuture.completedFuture(documentMapper.toResponse(savedDocument));


        } catch (IOException e) {
            log.error("Failed to create upload directory: {}", uploadDir, e);
            throw new ResourceNotFoundException("Could not create upload directory");
        }


    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<DocumentResponse> getDocument(Long id) {
        log.info("Fetching document with ID: {}", id);
        Document document = reposiotryService.retriveDocumentById(id);
        DocumentResponse response = documentMapper.toResponse(document);


        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<List<DocumentResponse>> getAllDocuments() {
        log.info("Fetching all documents");
        List<Document> documents = reposiotryService.retriveAllDocuments();
        List<DocumentResponse> documentResponses = documents.stream()
                .map(documentMapper::toResponse)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(documentResponses);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<DocumentResponse>> getDocumentsByUser(String uploadedBy) {
        log.info("Fetching documents uploaded by: {}", uploadedBy);
        List<Document> documents = reposiotryService.retriveAllDocumentsByUsers(uploadedBy);
        List<DocumentResponse> documentResponses = documents.stream()
                .map(documentMapper::toResponse)
                .collect(Collectors.toList());


        return CompletableFuture.completedFuture(documentResponses);
    }

    @Override
    public CompletableFuture<DocumentResponse> updateDocument(Long documentId, DocumentRequest request) {
        if (documentId == null) {
            throw new IllegalArgumentException("Claim id can not be null");

        }
        ReentrantLock lock = docLocks.computeIfAbsent(documentId, id -> new ReentrantLock());
        boolean lockAcquired = false;
        try {
            if (lock.tryLock(5, TimeUnit.MILLISECONDS)) {
                lockAcquired = true;
            }
            Document document = reposiotryService.retriveDocumentById(documentId);
            documentMapper.updateEntityFromRequest(request, document);
            Document savedDocument = reposiotryService.saveDocument(document);
            DocumentResponse documentResponce = documentMapper.toResponse(savedDocument);
            return CompletableFuture.completedFuture(documentResponce);


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (lockAcquired) {
                lock.unlock();
            }
        }

    }

    @Override
    public CompletableFuture<Void> deleteDocument(Long id) {
        Document document = reposiotryService.retriveDocumentById(id);
        try {
            Files.deleteIfExists(Paths.get(document.getContentPath()));
        } catch (IOException e) {
            log.warn("Failed to delete file: {}", e.getMessage());
        }
        reposiotryService.deleteDocument(id);

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<String> extractText(Long id) {
        log.info("Extracting text from document with ID: {}", id);

        Document document = reposiotryService.retriveDocumentById(id);

        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(document.getContentPath())));
            return huggingFaceService.extractTextFromDocument(fileContent)
                    .toFuture();
        } catch (IOException e) {
            log.error("Failed to read file", e);
            throw new ResourceNotFoundException("Failed to read file: " + e.getMessage());
        }
    }


    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResourceNotFoundException("File is empty");
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB
            throw new ResourceNotFoundException("File is too large (max 10 MB)");
        }


    }
}
