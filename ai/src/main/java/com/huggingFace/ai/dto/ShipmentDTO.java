package com.huggingFace.ai.dto;

import com.huggingFace.ai.domain.enums.EcartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentDTO {
    private String trackingNumber;
    private EcartStatus status;
    private LocalDateTime shipmentDate;
    private LocalDateTime expectedDeliveryDate;
    private String deliveryAddress;
}