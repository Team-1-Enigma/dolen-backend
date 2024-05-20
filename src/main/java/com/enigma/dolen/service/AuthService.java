package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterRequest;
import com.enigma.dolen.model.dto.RegisterResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    RegisterResponse register(RegisterRequest registerRequest, String url);
}
