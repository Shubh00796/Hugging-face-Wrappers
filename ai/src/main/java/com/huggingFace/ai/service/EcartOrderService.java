package com.huggingFace.ai.service;

import com.huggingFace.ai.domain.enums.EcartStatus;
import com.huggingFace.ai.dto.EcartOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EcartOrderService {

    Page<EcartOrderDTO> getAllOrders(Pageable pageable);

    EcartOrderDTO getById(Long id);

    EcartOrderDTO saveOrder(EcartOrderDTO ecartOrderDTO);

    void delete(Long id);

    EcartOrderDTO getByOrderByNumber(String orderNumber);

    List<EcartOrderDTO> getByOrderStatus(EcartStatus status);


}

