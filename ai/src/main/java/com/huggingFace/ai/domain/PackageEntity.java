package com.huggingFace.ai.domain;

import com.huggingFace.ai.domain.enums.EcartStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "packages")
@Data
public class PackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_id")
    private String packageId;


    private EcartStatus status;

    @Column(name = "delivery_address")
    private String deliveryAddress;
}