package com.enigma.dolen.service.impl;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserCredential> findByEmail(String email) {
        return userCredentialRepository.findByEmail(email);
    }

    @Override
    public UserCredential createCredential(RegisterRequest registerRequest, User user, Role role) {
        return userCredentialRepository.save(UserCredential.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .user(user)
                .build());
    }

    @Override
    public UserCredential findById(String id) {
        return userCredentialRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserById(String credentialId) {
        return userCredentialRepository.findById(credentialId).orElseThrow(() -> new UsernameNotFoundException("Invalid credential"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userCredentialRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid credential"));
    }
}
