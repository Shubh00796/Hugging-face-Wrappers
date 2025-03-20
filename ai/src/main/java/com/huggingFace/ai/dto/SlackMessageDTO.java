package com.huggingFace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackMessageDTO {
    private Long id;
    private String slackMessageId;
    private String slackChannelId;
    private String content;
    private String userId;
    private Instant timestamp;
}