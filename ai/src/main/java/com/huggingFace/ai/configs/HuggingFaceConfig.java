package com.huggingFace.ai.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class HuggingFaceConfig {

    @Value("${HUGGING_FACE_API_TOKEN}")
    private String apiToken;

    @Value("${HUGGING_FACE_API_BASE_URL}")
    private String baseUrl;

    @Bean
    public WebClient huggingFaceWebClient() {

        // Configure HTTP client with timeouts
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // Connection timeout
                .responseTimeout(Duration.ofSeconds(10)) // Response timeout
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS)) // Read timeout
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS))); // Write timeout

        // Configure memory limits for large payloads (2MB)
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl.trim())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiToken)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }
}