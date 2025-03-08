package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.request.ProjectRequest;
import com.huggingFace.ai.dto.response.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse getProject(UUID id);

    ProjectResponse getProjectByApiKey(String apiKey);

    List<ProjectResponse> getAllProjects();

    ProjectResponse updateProject(UUID id, ProjectRequest request);

    void deleteProject(UUID id);

    boolean validateApiKey(String apiKey);
}