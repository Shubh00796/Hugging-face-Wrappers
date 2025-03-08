package com.huggingFace.ai.repository;

import com.huggingFace.ai.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByApiKey(String apiKey);

    boolean existsByName(String name);
}