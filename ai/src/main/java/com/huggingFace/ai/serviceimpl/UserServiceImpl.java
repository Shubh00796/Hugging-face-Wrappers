package com.huggingFace.ai.serviceimpl;


import com.huggingFace.ai.configs.UserDetailsServiceImp;
import com.huggingFace.ai.domain.User;
import com.huggingFace.ai.dto.LoginUserDTO;
import com.huggingFace.ai.dto.UserDTO;
import com.huggingFace.ai.mapper.UserMapper;
import com.huggingFace.ai.repository.UserRepository;
import com.huggingFace.ai.service.UserService;
import com.huggingFace.ai.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;
    private final UserDetailsServiceImp userDetailsServiceImp;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userMapper.toDTO(userRepository.findById(id).orElseThrow());
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public UserDTO login(LoginUserDTO loginUserDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = authentication.getName();
        UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(username);
        System.out.println("User details: " + userDetails); // Check if userDetails is null
        String token = jwtUtil.generateToken(userDetails);
        System.out.println("Generated token: " + token); // Check if token is null
        User user = userRepository.findByUsername(username).orElseThrow();
        UserDTO userDTO = userMapper.toDTO(user);
        userDTO.setToken(token);
        return userDTO;
    }

}