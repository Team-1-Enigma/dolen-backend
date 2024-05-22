package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.entity.*;
import com.enigma.dolen.model.exception.ApplicationException;
import com.enigma.dolen.security.JwtUtil;
import com.enigma.dolen.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterRequest;
import com.enigma.dolen.model.dto.RegisterResponse;
import com.enigma.dolen.model.dto.UserDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final UserCredentialService userCredentialService;
    private final UserVerificationService userVerificationService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUser appUser = (AppUser) authentication.getPrincipal();
        UserCredential userCredential = userCredentialService.findByEmail(appUser.getEmail())
                .orElseThrow(() -> new ApplicationException("User not found", HttpStatus.NOT_FOUND));
        UserVerification userVerification = userCredential.getVerification();
        if (!userVerification.getIsVerified()) {
            throw new ApplicationException("Email not verified", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(appUser);

        return LoginResponse.builder()
                .token(token)
                .credentialId(appUser.getId())
                .email(appUser.getEmail())
                .role(String.valueOf(appUser.getRole()))
                .build();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public RegisterResponse register(RegisterRequest registerRequest, String url) {
        if (userCredentialService.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new ApplicationException("Email already registered", HttpStatus.BAD_REQUEST);
        }
        UserDTO userDTO = UserDTO.builder()
                .fullName(registerRequest.getFullName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
        User user = userService.createUser(userDTO);
        Role role = roleService.getOrSave(ERole.USER);
        UserCredential userCredential = userCredentialService.createCredential(registerRequest, user, role);
//        UserVerification userVerification = userVerificationService.createVerification(userCredential, url);
//        userCredential.setVerification(userVerification);

        return RegisterResponse.builder()
                .credentialId(userCredential.getId())
                .email(userCredential.getEmail())
                .build();
    }
}
