package com.enigma.dolen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterResponse;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .message("Login success")
                .statusCode(HttpStatus.OK.value())
                .data(loginResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        RegisterResponse registerResponse = authService.register(userDTO);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("Register success")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
