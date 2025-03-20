package com.huggingFace.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelDTO {
    private Long id;
    private String slackChannelId;
    private String name;
    private String owner;
}