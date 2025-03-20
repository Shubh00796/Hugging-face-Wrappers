package com.huggingFace.ai.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "slack_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slackMessageId;

    private String slackChannelId;

    @Lob
    private String content;

    private String userId;
    private Instant timestamp;
}