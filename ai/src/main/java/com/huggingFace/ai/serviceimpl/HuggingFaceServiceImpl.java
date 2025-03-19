package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.enums.ContentAnalysisType;
import com.huggingFace.ai.dto.request.ContentAnalysisRequest;
import com.huggingFace.ai.dto.response.ContentAnalysisResponse;
import com.huggingFace.ai.service.HuggingFaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HuggingFaceServiceImpl implements HuggingFaceService {
//    private static final Logger log = LoggerFactory.getLogger(HuggingFaceServiceImpl.class);

    private final WebClient huggingFaceWebClient;
    @Value("${huggingface.models.text-extraction}")
    private String textExtractionModel;

    @Value("${huggingface.models.text-analysis}")
    private String defaultAnalysisModel;

    @Value("${huggingface.models.summarization}")
    private String summarizationModel;

    @Value("${huggingface.models.key-insights}")
    private String keyInsightsModel;

    @Override
    public Mono<String> extractTextFromDocument(String documentContent) {
        log.info("Extracting text using model: {}", textExtractionModel);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", documentContent);

        return callHuggingFaceApi(textExtractionModel, requestBody);
    }

    @Override
    public Mono<String> analyzeText(String text, String modelId) {
        String analysisModel = modelId != null ? modelId : defaultAnalysisModel;
        log.info("Analyzing text using model: {}", analysisModel);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", text);

        return callHuggingFaceApi(analysisModel, requestBody);
    }

    @Override
    public Mono<String> generateSummary(String text) {
        log.info("Generating summary using model: {}", summarizationModel);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", text);
        requestBody.put("parameters", Map.of(
                "max_length", 150,
                "min_length", 50,
                "do_sample", false
        ));

        return callHuggingFaceApi(summarizationModel, requestBody);
    }

    @Override
    public Mono<String> extractKeyInsights(String text) {
        log.info("Extracting key insights using model: {}", keyInsightsModel);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", "Extract the key insights from this text: " + text);

        return callHuggingFaceApi(keyInsightsModel, requestBody);
    }



    private Mono<String> callHuggingFaceApi(String modelId, Map<String, Object> requestBody) {
        return huggingFaceWebClient.post()
                .uri(modelId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> log.error("Error calling Hugging Face API: {}", error.getMessage()))
                .onErrorResume(error -> Mono.just("Error processing request: " + error.getMessage()));
    }
    public Mono<ContentAnalysisResponse> analyzeContent(ContentAnalysisRequest request) {
        log.info("Processing content analysis: Type = {}, Model = {}", request.getType(), request.getModel());

        Map<String, Object> requestBody = buildRequestBody(request);
        String model = getModel(request);

        return huggingFaceWebClient.post()
                .uri("/{model}", model)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})  // ✅ Expecting a List<Map>
                .map(result -> buildResponse(request, result))  // ✅ Pass the whole list
                .onErrorResume(error -> {
                    log.error("Hugging Face API error: {}", error.getMessage());
                    return Mono.just(buildErrorResponse(request, error.getMessage()));
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    private String getModel(ContentAnalysisRequest request) {
        if (request.getModel() != null && !request.getModel().isEmpty()) {
            return request.getModel();
        }
        return switch (request.getType()) {
            case SENTIMENT -> "distilbert-base-uncased-finetuned-sst-2-english";
            case CLASSIFICATION -> "facebook/bart-large-mnli";
            case SUMMARIZATION -> "facebook/bart-large-cnn";
            case ENTITY_RECOGNITION -> "dbmdz/bert-large-cased-finetuned-conll03-english";
            case QUESTION_ANSWERING -> "deepset/roberta-base-squad2";
            case TRANSLATION -> "t5-base";
        };
    }

    private Map<String, Object> buildRequestBody(ContentAnalysisRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("inputs", request.getContent());

        if (request.getType() == ContentAnalysisType.QUESTION_ANSWERING) {
            if (request.getOptions() == null || !request.getOptions().containsKey("question") || !request.getOptions().containsKey("context")) {
                throw new IllegalArgumentException("Question Answering requires 'question' and 'context' in options.");
            }
            body.put("question", request.getOptions().get("question"));
            body.put("context", request.getOptions().get("context"));
        }

        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            body.putAll(request.getOptions());
        }

        return body;
    }

    private ContentAnalysisResponse buildResponse(ContentAnalysisRequest request, List<Map<String, Object>> result) {
        return ContentAnalysisResponse.builder()
                .id(UUID.randomUUID())
                .type(request.getType().name())
                .result(result)  // ✅ Now stores full list instead of a single Map
                .modelUsed(getModel(request))
                .processingTime(System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .status("SUCCESS")
                .errorMessage(null)
                .build();
    }

    private ContentAnalysisResponse buildErrorResponse(ContentAnalysisRequest request, String errorMessage) {
        return ContentAnalysisResponse.builder()
                .id(UUID.randomUUID())
                .type(request.getType().name())
                .result(Collections.emptyList())  // ✅ Ensures result is never null
                .modelUsed(getModel(request))
                .processingTime(System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .status("FAILURE")
                .errorMessage(errorMessage)
                .build();
    }
}
