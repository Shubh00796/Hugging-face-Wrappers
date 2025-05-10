package com.huggingFace.ai.configs;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class TrackingWebSocketHandler extends TextWebSocketHandler {
    private final Map<WebSocketSession, String> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("WebSocket connection closed");
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String trackingNumber = message.getPayload();
        sessions.put(session, trackingNumber);
        System.out.println("Received tracking number: " + trackingNumber);
    }

    public void sendUpdate(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            System.out.println("Error sending update: " + e.getMessage());
        }
    }

    public void broadcastUpdate(String message) {
        sessions.forEach((session, trackingNumber) -> sendUpdate(session, message));
    }
}