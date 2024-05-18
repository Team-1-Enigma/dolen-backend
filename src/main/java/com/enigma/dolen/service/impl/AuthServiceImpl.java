package com.enigma.dolen.service.impl;

import org.springframework.stereotype.Service;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterResponse;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.Role;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.service.AuthService;
import com.enigma.dolen.service.RoleService;
import com.enigma.dolen.service.UserCredentialService;
import com.enigma.dolen.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final UserCredentialService userCredentialService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserCredential userCredential = userCredentialService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!userCredential.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return LoginResponse.builder()
                .id(userCredential.getId())
                .token("token")
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse register(UserDTO userDTO) {
        User user = userService.createUser(userDTO);
        Role role = roleService.getOrSave(ERole.USER);
        UserCredential userCredential = userCredentialService.createCredential(userDTO, user, role);

        return RegisterResponse.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .role(userCredential.getRole().getName().toString())
                .build();
    }
}
