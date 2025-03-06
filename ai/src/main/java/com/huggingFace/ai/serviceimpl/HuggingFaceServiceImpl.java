package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.service.HuggingFaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

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
}
