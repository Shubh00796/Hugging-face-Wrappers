package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Optional<Channel> findBySlackChannelId(String slackChannelId);
}