package com.huggingFace.ai.reposiotryservices;

import com.huggingFace.ai.domain.Tracking;
import com.huggingFace.ai.dto.ShipmentDTO;
import com.huggingFace.ai.dto.TrackingDTO;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrackingReposiotryService {
    private final TrackingRepository trackingRepository;


    public List<Tracking> findAll() {
        return trackingRepository.findAll();
    }

    public Tracking saveTracking(Tracking tracking) {
        return trackingRepository.save(tracking);
    }

    public Tracking findOrderById(Long id) {
        return trackingRepository.findById(id).orElseThrow();
    }

    public Tracking findByTrackingNumber(String trackingNumber) {
        return trackingRepository.findByTrackingNumber(trackingNumber).orElseThrow();
    }


}
