package com.huggingFace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnomalyDetectedEvent {
    private String deviceIdentifier;
    private String severity;
    private Instant timestamp;
}