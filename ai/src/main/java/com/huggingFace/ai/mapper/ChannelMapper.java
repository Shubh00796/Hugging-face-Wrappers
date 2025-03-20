package com.huggingFace.ai.mapper;

import com.huggingFace.ai.domain.Channel;
import com.huggingFace.ai.domain.Document;
import com.huggingFace.ai.dto.ChannelDTO;
import com.huggingFace.ai.dto.request.DocumentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    @Mapping(target = "id", ignore = true)
    ChannelDTO toDto(Channel channel);

    @Mapping(target = "id", ignore = true)
    Channel toEntity(ChannelDTO channelDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slackChannelId", ignore = true)
    void updateChannelEntityFromDto(ChannelDTO channelDTO, @MappingTarget Channel channel);


}