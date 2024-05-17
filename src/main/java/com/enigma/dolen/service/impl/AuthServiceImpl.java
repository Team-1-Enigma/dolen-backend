package com.enigma.dolen.service.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterResponse;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.Role;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.repository.UserCredentialRepository;
import com.enigma.dolen.service.AuthService;
import com.enigma.dolen.service.RoleService;
import com.enigma.dolen.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // private final UserService userService;
    private final RoleService roleService;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserCredential userCredential = userCredentialRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!userCredential.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new LoginResponse(userCredential.getUser().getId(), userCredential.getEmail());
    }

    @Transactional
    @Override
    public RegisterResponse register(UserDTO userDTO) {
        try {
            User user = User.builder()
                    .fullName(userDTO.getFullName())
                    .build();
            // userService.createUser(user);

            Role role = roleService.getOrSave(ERole.USER);

            UserCredential userCredential = UserCredential.builder()
                    .email(userDTO.getEmail())
                    .password(userDTO.getPassword())
                    .role(role)
                    .user(user)
                    .build();
            userCredentialRepository.save(userCredential);

            return RegisterResponse.builder()
                    .id(userCredential.getUser().getId())
                    .email(userCredential.getEmail())
                    .role(userCredential.getRole().getName().toString())
                    .build();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Email already registered");
        }
    }
}
