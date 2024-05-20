package com.enigma.dolen.controller;

import com.enigma.dolen.model.entity.UserVerification;
import com.enigma.dolen.service.UserVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterRequest;
import com.enigma.dolen.model.dto.RegisterResponse;
import com.enigma.dolen.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserVerificationService userVerificationService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login success")
                .data(loginResponse)
                .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<?>> register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        RegisterResponse registerResponse = authService.register(registerRequest, getSiteURL(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.<RegisterResponse>builder()
                .message("Register success")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build()
        );
    }

    private String getSiteURL (HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
