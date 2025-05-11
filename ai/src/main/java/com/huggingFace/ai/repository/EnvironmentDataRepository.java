package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.EnvironmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentDataRepository extends JpaRepository<EnvironmentData, Long> {
}