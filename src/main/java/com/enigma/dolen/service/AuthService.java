package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.LoginRequest;
import com.enigma.dolen.model.dto.LoginResponse;
import com.enigma.dolen.model.dto.RegisterResponse;
import com.enigma.dolen.model.dto.UserDTO;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    RegisterResponse register(UserDTO userDTO);
}
