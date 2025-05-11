package com.huggingFace.ai.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnvironmentalConditions {
    private Double temperature;
    private Double humidity;
    private Double airQuality;

    public boolean isWithinSafeRange() {
        return temperature >= 15 && temperature <= 30 &&
                humidity >= 20 && humidity <= 80 &&
                airQuality >= 0 && airQuality <= 50;
    }
}