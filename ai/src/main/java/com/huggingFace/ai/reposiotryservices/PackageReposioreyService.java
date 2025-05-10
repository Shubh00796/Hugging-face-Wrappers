package com.huggingFace.ai.reposiotryservices;

import com.huggingFace.ai.domain.PackageEntity;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PackageReposioreyService {
    private final PackageRepository packageRepository;

    public PackageEntity savePacakage(PackageEntity packageEntity) {
        return packageRepository.save(packageEntity);
    }

//    public PackageEntity getId(Long id) {
//        PackageEntity packageEntity = packageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id not found" + id));
//        return packageEntity;
//
//    }

    public List<PackageEntity> getAllPacakges() {
        return packageRepository.findAll();
    }

    public Optional<PackageEntity> getPackageById(Long id) {
        return packageRepository.findById(id);
    }

    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }
}
