package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.EcartOrder;
import com.huggingFace.ai.domain.enums.EcartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EcartOrderRepository extends JpaRepository<EcartOrder, Long> {
    Optional<EcartOrder> findByOrderNumber(String orderNumber);

    List<EcartOrder> findByStatus(EcartStatus status);

}