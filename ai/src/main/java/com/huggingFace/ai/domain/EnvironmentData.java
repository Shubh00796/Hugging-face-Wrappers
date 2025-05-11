package com.huggingFace.ai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@Table(name = "environment_data")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnvironmentData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(nullable = false)
    private Double airQuality;

    @Column(nullable = false)
    private String deviceIdentifier;

    @Column(nullable = false)
    private Instant timestamp;
}