package com.huggingFace.ai.service;

import com.huggingFace.ai.dto.LoginUserDTO;
import com.huggingFace.ai.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO login(LoginUserDTO loginUserDTO);
}