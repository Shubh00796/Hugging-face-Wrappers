package com.huggingFace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnvironmentDataDTO {
    private Double temperature;
    private Double humidity;
    private Double airQuality;
    private String deviceIdentifier;
}