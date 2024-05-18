package com.enigma.dolen.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.enigma.dolen.model.entity.Role;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.repository.UserCredentialRepository;
import com.enigma.dolen.service.UserCredentialService;
import com.enigma.dolen.model.dto.RegisterRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;

    @Override
    public Optional<UserCredential> findByEmail(String email) {
        return userCredentialRepository.findByEmail(email);
    }

    @Override
    public UserCredential createCredential(RegisterRequest registerRequest, User user, Role role) {
        return userCredentialRepository.save(UserCredential.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .role(role)
                .user(user)
                .build());
    }

    @Override
    public UserCredential findById(String id) {
        return userCredentialRepository.findById(id).orElse(null);
    }

}
