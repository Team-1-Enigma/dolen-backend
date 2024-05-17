package com.enigma.dolen.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.entity.Role;
import com.enigma.dolen.repository.RoleRepository;
import com.enigma.dolen.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(ERole role) {
        Optional<Role> optionalRole = roleRepository.findByName(role);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }

        Role newRole = Role.builder()
                .name(role)
                .build();

        return roleRepository.save(newRole);
    }
}
