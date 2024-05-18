package com.enigma.dolen.service.impl;

import com.enigma.dolen.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterRequest;
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
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserCredential userCredential = (UserCredential) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userCredential);

        return LoginResponse.builder()
                .credentialId(userCredential.getId())
                .token(token)
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        UserDTO userDTO = UserDTO.builder()
                .fullName(registerRequest.getFullName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
        User user = userService.createUser(userDTO);
        Role role = roleService.getOrSave(ERole.USER);
        UserCredential userCredential = userCredentialService.createCredential(registerRequest, user, role);

        return RegisterResponse.builder()
                .credentialId(userCredential.getId())
                .email(userCredential.getEmail())
                .build();
    }
}
