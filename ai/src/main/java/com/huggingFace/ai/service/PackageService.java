package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.PackageDTO;

import java.util.List;
import java.util.Optional;

public interface PackageService {
    PackageDTO createPackage(PackageDTO packageDTO);
    List<PackageDTO> getAllPackages();
    Optional<PackageDTO> getPackageById(Long id);
    PackageDTO updatePackage(PackageDTO packageDTO);
    void deletePackage(Long id);
}