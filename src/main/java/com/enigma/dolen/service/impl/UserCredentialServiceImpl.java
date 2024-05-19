package com.enigma.dolen.service.impl;

import java.util.Optional;

import com.enigma.dolen.model.entity.*;
import com.enigma.dolen.model.exception.ApplicationException;
import com.enigma.dolen.service.UserVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return userCredentialRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("User credential not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public AppUser loadUserById(String credentialId) {
        UserCredential userCredential =  userCredentialRepository.findById(credentialId)
                .orElseThrow(() -> new ApplicationException("User credential not found", HttpStatus.NOT_FOUND));
        return AppUser.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .role(userCredential.getRole().getName())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credential"));
        return AppUser.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .role(userCredential.getRole().getName())
                .build();
    }
}
