package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.SlackMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, Long> {
    Page<SlackMessage> findBySlackChannelId(String slackChannelId, Pageable pageable);
}