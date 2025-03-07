package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.Project;
import com.huggingFace.ai.dto.request.ProjectRequest;
import com.huggingFace.ai.dto.response.ProjectResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "apiKey", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Project toEntity(ProjectRequest request);

    ProjectResponse toResponse(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectFromRequest(ProjectRequest request, @MappingTarget Project project);
}