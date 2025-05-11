package com.huggingFace.ai.mapper;


import com.huggingFace.ai.domain.User;
import com.huggingFace.ai.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(UserDTO userDTO);
}