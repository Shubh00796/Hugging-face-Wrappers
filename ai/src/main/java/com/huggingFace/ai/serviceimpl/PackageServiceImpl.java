package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.PackageEntity;
import com.huggingFace.ai.domain.enums.EcartStatus;
import com.huggingFace.ai.dto.PackageDTO;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.mapper.PackageMapper;
import com.huggingFace.ai.reposiotryservices.PackageReposioreyService;
import com.huggingFace.ai.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {
    private final PackageMapper packageMapper;
    private final PackageReposioreyService reposioreyService;

    @Override
    public PackageDTO createPackage(PackageDTO packageDTO) {
        PackageEntity entity = packageMapper.toEntity(packageDTO);
        entity.setStatus(EcartStatus.PENDING);
        entity.setPackageId(generatePackageId());
        PackageEntity packageEntity = reposioreyService.savePacakage(entity);


        return packageMapper.toDTO(packageEntity);
    }

    private String generatePackageId() {
        return UUID.randomUUID().toString();
    }


    @Override
    public List<PackageDTO> getAllPackages() {
        return reposioreyService.getAllPacakges()
                .stream()
                .map(packageMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PackageDTO> getPackageById(Long id) {
        return reposioreyService.getPackageById(id)
                .map(packageMapper::toDTO);
    }

    @Override
    public PackageDTO updatePackage(PackageDTO packageDTO) {
        PackageEntity existingPackage = reposioreyService.getPackageById(packageDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + packageDTO.getPackageId()));

        existingPackage.setPackageId(packageDTO.getPackageId());
        existingPackage.setDeliveryAddress(packageDTO.getDeliveryAddress());
        existingPackage.setStatus(packageDTO.getStatus());

        PackageEntity updatedPackage = reposioreyService.savePacakage(existingPackage);
        return packageMapper.toDTO(updatedPackage);
    }

    @Override
    public void deletePackage(Long id) {
        reposioreyService.deletePackage(id);

    }
}
