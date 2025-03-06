package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.Document;
import com.huggingFace.ai.dto.request.DocumentRequest;
import com.huggingFace.ai.dto.response.DocumentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contentPath", ignore = true)
    @Mapping(target = "uploadedAt", expression = "java(java.time.LocalDateTime.now())")
    Document toEntity(DocumentRequest request);

    DocumentResponse toResponse(Document document);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contentPath", ignore = true)
    @Mapping(target = "uploadedAt", ignore = true)
    void updateEntityFromRequest(DocumentRequest request, @MappingTarget Document document);


}
