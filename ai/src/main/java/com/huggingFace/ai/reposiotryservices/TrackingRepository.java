package com.huggingFace.ai.reposiotryservices;

import com.huggingFace.ai.domain.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {
    Tracking findByTrackingNumber(String trackingNumber);
}