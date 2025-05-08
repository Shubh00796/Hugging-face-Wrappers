package com.huggingFace.ai.controller;

import com.huggingFace.ai.domain.enums.EcartStatus;
import com.huggingFace.ai.dto.EcartOrderDTO;
import com.huggingFace.ai.service.EcartOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class EcartOrderController {
    private final EcartOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<EcartOrderDTO> getOrderById(@PathVariable @NonNull Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EcartOrderDTO> createOrder(@RequestBody @Valid EcartOrderDTO ecartOrderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.saveOrder(ecartOrderDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable @NotNull Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<EcartOrderDTO> getByOrderNumber(@PathVariable @NotBlank String orderNumber) {
        return ResponseEntity.ok(orderService.getByOrderByNumber(orderNumber));
    }

    @GetMapping
    public ResponseEntity<Page<EcartOrderDTO>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EcartOrderDTO>> getByOrderStatus(@PathVariable @NotNull EcartStatus status) {
        return ResponseEntity.ok(orderService.getByOrderStatus(status));
    }


}
