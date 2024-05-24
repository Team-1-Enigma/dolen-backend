package com.enigma.dolen.service;

import java.util.Optional;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.entity.AppUser;
import com.enigma.dolen.model.entity.Role;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.model.dto.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserCredentialService extends UserDetailsService {

    Optional<UserCredential> findByEmail(String email);

    UserCredential createCredential(RegisterRequest registerRequest, User user, Role role);

    UserCredential findById(String id);

    AppUser loadUserById(String credentialId);

    void changeUserRole(ERole eRole, String credentialId);
}
