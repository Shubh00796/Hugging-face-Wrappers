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
public class PackageDTO {
    private Long id;
    private String packageId;
    private EcartStatus status;
    private String deliveryAddress;
}
