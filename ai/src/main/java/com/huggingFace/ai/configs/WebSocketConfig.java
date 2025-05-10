package com.huggingFace.ai.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final String TRACKING_WEBSOCKET_ENDPOINT = "/tracking/websocket";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(trackingWebSocketHandler(), TRACKING_WEBSOCKET_ENDPOINT).setAllowedOrigins("*");
    }

    @Bean
    public TrackingWebSocketHandler trackingWebSocketHandler() {
        return new TrackingWebSocketHandler();
    }
}