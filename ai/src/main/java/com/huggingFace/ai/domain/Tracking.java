package com.huggingFace.ai.domain;

import com.huggingFace.ai.domain.enums.EcartStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tracking")
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private EcartStatus status;

    private LocalDateTime shipmentDate;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

}
