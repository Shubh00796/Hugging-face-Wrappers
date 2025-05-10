package com.huggingFace.ai.controller;

import com.huggingFace.ai.dto.ShipmentDTO;
import com.huggingFace.ai.dto.TrackingDTO;
import com.huggingFace.ai.service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping("/shipments")
    public ResponseEntity<TrackingDTO> createShipment(@Valid @RequestBody ShipmentDTO shipmentDTO) {
        TrackingDTO trackingDTO = trackingService.createShipment(shipmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(trackingDTO);
    }

    @GetMapping("/shipments")
    public ResponseEntity<List<ShipmentDTO>> getAllShipments() {
        List<ShipmentDTO> shipments = trackingService.getAllShipments();
        return ResponseEntity.ok(shipments);
    }

    @PutMapping("/trackings/{id}")
    public ResponseEntity<TrackingDTO> updateTracking(@PathVariable Long id, @Valid @RequestBody TrackingDTO trackingDTO) {
        TrackingDTO updatedTrackingDTO = trackingService.updateTracking(id, trackingDTO);
        return ResponseEntity.ok(updatedTrackingDTO);
    }

    @GetMapping("/trackings/{id}")
    public ResponseEntity<TrackingDTO> getTrackingDetails(@PathVariable Long id) {
        TrackingDTO trackingDTO = trackingService.getTrackingDetails(id);
        return ResponseEntity.ok(trackingDTO);
    }

    @GetMapping("/trackings/number/{trackingNumber}")
    public ResponseEntity<TrackingDTO> getTrackingByNumber(@PathVariable String trackingNumber) {
        TrackingDTO trackingDTO = trackingService.getTrackingByNumber(trackingNumber);
        return ResponseEntity.ok(trackingDTO);
    }
}