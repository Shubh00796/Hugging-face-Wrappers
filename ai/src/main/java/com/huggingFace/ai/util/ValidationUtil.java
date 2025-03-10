package com.huggingFace.ai.util;

import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidationUtil {

    public void validateAnalysisRequest(ContentAnalysisRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        if (request.getContent().length() > 10000) {
            throw new IllegalArgumentException("Content must be less than 10000 characters");
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException("Analysis type is required");
        }

        // Validate specific requirements for each analysis type
        switch (request.getType()) {
            case QUESTION_ANSWERING:
                validateQuestionAnsweringRequest(request);
                break;
            case TRANSLATION:
                validateTranslationRequest(request);
                break;
            default:
                // Other types don't need special validation
                break;
        }
    }

    private void validateQuestionAnsweringRequest(ContentAnalysisRequest request) {
        if (request.getOptions() == null) {
            throw new IllegalArgumentException("Question answering requires 'question' and 'context' options");
        }

        if (!request.getOptions().containsKey("question")) {
            throw new IllegalArgumentException("Question answering requires a 'question' option");
        }

        if (!request.getOptions().containsKey("context")) {
            throw new IllegalArgumentException("Question answering requires a 'context' option");
        }
    }

    private void validateTranslationRequest(ContentAnalysisRequest request) {
        if (request.getOptions() == null || !request.getOptions().containsKey("targetLanguage")) {
            throw new IllegalArgumentException("Translation requires a 'targetLanguage' option");
        }
    }
}