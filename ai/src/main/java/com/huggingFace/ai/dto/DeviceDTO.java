package com.huggingFace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private Long id;
    private String identifier;
    private String deviceType;
    private String location;
    private Instant registrationDate;
}