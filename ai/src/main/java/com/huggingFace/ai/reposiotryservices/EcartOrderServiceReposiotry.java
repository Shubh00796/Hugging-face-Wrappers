package com.huggingFace.ai.reposiotryservices;

import com.huggingFace.ai.domain.EcartOrder;
import com.huggingFace.ai.domain.enums.EcartStatus;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.repository.EcartOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EcartOrderServiceReposiotry {
    private final EcartOrderRepository orderRepository;

    public EcartOrder svaeOrder(EcartOrder ecartOrder) {
        return orderRepository.save(ecartOrder);
    }

    public EcartOrder findOrderById(Long id) {
        return getOrderById(id);
    }

    public EcartOrder findOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order with number " + orderNumber + " not found."));
    }

    private EcartOrder getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + id + " not found."));
    }

    public Page<EcartOrder> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public List<EcartOrder> findOrdersByStatus(EcartStatus status) {
        return orderRepository.findByStatus(status);
    }

    public void deleteOrder(Long id) {
        orderRepository.delete(getOrderById(id));


    }

}
