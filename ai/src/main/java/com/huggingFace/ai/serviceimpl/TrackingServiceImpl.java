package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.configs.TrackingWebSocketHandler;
import com.huggingFace.ai.domain.Tracking;
import com.huggingFace.ai.domain.enums.EcartStatus;
import com.huggingFace.ai.dto.ShipmentDTO;
import com.huggingFace.ai.dto.TrackingDTO;
import com.huggingFace.ai.mapper.ShipmentMapper;
import com.huggingFace.ai.mapper.TrackingMapper;
import com.huggingFace.ai.reposiotryservices.TrackingReposiotryService;
import com.huggingFace.ai.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    private final TrackingWebSocketHandler trackingWebSocketHandler;
    private final TrackingMapper trackingMapper;
    private final ShipmentMapper shipmentMapper;
    private final TrackingReposiotryService reposiotryService;

    @Override
    public TrackingDTO createShipment(ShipmentDTO shipmentDTO) {
        Tracking tracking = shipmentMapper.toEntity(shipmentDTO);
        tracking.setTrackingNumber(generateTrackingNumber());
        tracking.setShipmentDate(LocalDateTime.now());
        tracking.setLastUpdated(LocalDateTime.now());
        tracking.setStatus(EcartStatus.PENDING);

        return trackingMapper.toDTO(reposiotryService.saveTracking(tracking));
    }


    @Override
    public List<ShipmentDTO> getAllShipments() {
        List<Tracking> trackings = reposiotryService.findAll();
        return trackings.stream().map(tracking -> shipmentMapper.toDTO(tracking)).collect(Collectors.toList());
    }

    @Override
    public TrackingDTO updateTracking(Long id, TrackingDTO trackingDTO) {
        Tracking tracking = reposiotryService.findOrderById(id);
        trackingMapper.updateFromDTO(tracking, trackingDTO);
        tracking.setStatus(EcartStatus.PROCESSING);

        try {
            String message = "Tracking updated: " + trackingDTO.getTrackingNumber();
            trackingWebSocketHandler.broadcastUpdate(message);
        } catch (Exception e) {
            System.out.println("Error sending update to WebSocket endpoint: " + e.getMessage());
        }

        Tracking savedTracking = reposiotryService.saveTracking(tracking);
        return trackingMapper.toDTO(savedTracking);
    }

    @Override
    public TrackingDTO getTrackingDetails(Long id) {
        Tracking orderById = reposiotryService.findOrderById(id);
        return trackingMapper.toDTO(orderById);
    }

    public TrackingDTO getTrackingByNumber(String trackingNumber) {
        Tracking tracking = reposiotryService.findByTrackingNumber(trackingNumber);
        return trackingMapper.toDTO(tracking);
    }

    private String generateTrackingNumber() {
        return "TRACK-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}