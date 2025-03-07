package com.huggingFace.ai.reposiotryservices;

import com.huggingFace.ai.domain.Document;
import com.huggingFace.ai.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.module.ResolutionException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DocumentReposiotryService {
    private final DocumentRepository documentRepository;

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public Document retriveDocumentById(Long documentId) {
        return getDocumentById(documentId);
    }

    public List<Document> retriveAllDocuments() {
        return documentRepository.findAll();
    }

    public List<Document> retriveAllDocumentsByUsers(String uploadedBy) {
        return documentRepository.findByUploadedBy(uploadedBy);
    }

    public void deleteDocument(Long documentId) {
        Document document = getDocumentById(documentId);

        documentRepository.delete(document);
    }


    public Document extarctTextFromDocument(Long documentId) {
        return getDocumentById(documentId);
    }

    private Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResolutionException("The document with given id not found" + documentId));
    }
}
