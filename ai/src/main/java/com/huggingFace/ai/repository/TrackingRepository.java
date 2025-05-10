package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.Tracking;
import com.huggingFace.ai.dto.ShipmentDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {
    Optional<Tracking> findByTrackingNumber(String trackingNumber);

    Optional<Tracking> findById(Long id);
}