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
public class EcartOrderDTO {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private EcartStatus status;
}
