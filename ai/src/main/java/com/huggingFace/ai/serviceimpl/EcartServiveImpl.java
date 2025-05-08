package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.EcartOrder;
import com.huggingFace.ai.domain.enums.EcartStatus;
import com.huggingFace.ai.dto.EcartOrderDTO;
import com.huggingFace.ai.mapper.OrderEcartMapper;
import com.huggingFace.ai.reposiotryservices.EcartOrderServiceReposiotry;
import com.huggingFace.ai.service.EcartOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EcartServiveImpl implements EcartOrderService {
    private final EcartOrderServiceReposiotry orderServiceReposiotry;
    private final OrderEcartMapper mapper;

    @Override
    public Page<EcartOrderDTO> getAllOrders(Pageable pageable) {
        Page<EcartOrder> orders = orderServiceReposiotry.findAllOrders(pageable);
        return orders.map(mapper::toDTO);
    }

    @Override
    public EcartOrderDTO getById(Long id) {
        EcartOrder orderById = orderServiceReposiotry.findOrderById(id);
        return mapper.toDTO(orderById);
    }



    @Override
    public EcartOrderDTO saveOrder(EcartOrderDTO ecartOrderDTO) {
        EcartOrder savedToEntitt = mapper.toEntity(ecartOrderDTO);
        EcartOrder ecartOrder = orderServiceReposiotry.svaeOrder(savedToEntitt); // Use save instead of svaeOrder
        return mapper.toDTO(ecartOrder);
    }

    @Override
    public void delete(Long id) {
        orderServiceReposiotry.deleteOrder(id);


    }

    @Override
    public EcartOrderDTO getByOrderByNumber(String orderNumber) {
        EcartOrder order = orderServiceReposiotry.findOrderByOrderNumber(orderNumber);
        return mapper.toDTO(order);
    }

    @Override
    public List<EcartOrderDTO> getByOrderStatus(EcartStatus status) {
        List<EcartOrder> ordersByStatus = orderServiceReposiotry.findOrdersByStatus(status);
        return ordersByStatus.stream().map(mapper::toDTO).collect(Collectors.toList());
    }
}
