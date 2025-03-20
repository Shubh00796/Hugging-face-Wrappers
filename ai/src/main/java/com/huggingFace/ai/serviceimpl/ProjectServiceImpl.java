package com.huggingFace.ai.serviceimpl;

import com.huggingFace.ai.domain.Project;
import com.huggingFace.ai.dto.request.ProjectRequest;
import com.huggingFace.ai.dto.response.ProjectResponse;
import com.huggingFace.ai.exceptions.ResourceNotFoundException;
import com.huggingFace.ai.mapper.ProjectMapper;
import com.huggingFace.ai.repository.ProjectRepository;
import com.huggingFace.ai.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        log.info("Creating new project: {}", request.getName());

        if (projectRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Project with name '" + request.getName() + "' already exists");
        }

        Project project = projectMapper.toEntity(request);
        project.setCreatedAt(LocalDateTime.now());
        project.setApiKey(generateApiKey());

        Project savedProject = projectRepository.save(project);
        log.info("Project created with ID: {}", savedProject.getId());

        return projectMapper.toResponse(savedProject);
    }

    @Override
    public ProjectResponse getProject(UUID id) {
        log.info("Retrieving project with ID: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse getProjectByApiKey(String apiKey) {
        log.info("Retrieving project by API key");
        Project project = projectRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with the provided API key"));

        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        log.info("Retrieving all projects");
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(UUID id, ProjectRequest request) {
        log.info("Updating project with ID: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        projectMapper.updateProjectFromRequest(request, project);
        project.setUpdatedAt(LocalDateTime.now());

        Project updatedProject = projectRepository.save(project);
        log.info("Project updated: {}", updatedProject.getId());

        return projectMapper.toResponse(updatedProject);
    }

    @Override
    @Transactional
    public void deleteProject(UUID id) {
        log.info("Deleting project with ID: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        projectRepository.delete(project);
        log.info("Project deleted: {}", id);
    }

    @Override
    public boolean validateApiKey(String apiKey) {
        return projectRepository.findByApiKey(apiKey).isPresent();
    }

    private String generateApiKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}