package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.ShipmentDTO;
import com.huggingFace.ai.dto.TrackingDTO;

import java.util.List;

public interface TrackingService {
    TrackingDTO createShipment(ShipmentDTO shipmentDTO);

    List<ShipmentDTO> getAllShipments();

    TrackingDTO updateTracking(Long id, TrackingDTO trackingDTO);
    TrackingDTO getTrackingDetails(Long id);
    TrackingDTO getTrackingByNumber(String trackingNumber);
}