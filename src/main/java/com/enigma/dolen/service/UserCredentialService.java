package com.enigma.dolen.service;

import java.util.Optional;

import com.enigma.dolen.model.entity.Role;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.model.dto.RegisterRequest;

public interface UserCredentialService {

    Optional<UserCredential> findByEmail(String email);

    UserCredential createCredential(RegisterRequest registerRequest, User user, Role role);

    UserCredential findById(String id);
}