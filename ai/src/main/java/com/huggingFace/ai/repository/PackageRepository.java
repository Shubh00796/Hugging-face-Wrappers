package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    Optional<PackageEntity> findByPackageId(String packageId);
}