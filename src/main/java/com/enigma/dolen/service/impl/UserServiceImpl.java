package com.enigma.dolen.service.impl;

import java.time.LocalDate;

import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.service.UserCredentialService;
import org.springframework.stereotype.Service;

import com.enigma.dolen.constant.EGender;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.UserRepository;
import com.enigma.dolen.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCredentialService userCredentialService;

    @Override
    public UserDTO getUserById(String id) {
        UserCredential userCredential = userCredentialService.findById(id);
        User user = userRepository.findById(userCredential.getUser().getId()).orElse(null);
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .credentialId(userCredential.getId())
                .email(userCredential.getEmail())
                .role(userCredential.getRole().getName().toString())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                // .gender(user.getGender().toString())
                // .address(user.getAddress())
                // .birthDate(user.getBirthDate().toString())
                // .photoUrl(user.getPhotoUrl())
                .isActive(user.getIsActive())
                .build();
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .isActive(true)
                .build();
        userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        UserCredential userCredential = userCredentialService.findById(id);
        User user = userRepository.findById(userCredential.getUser().getId()).orElse(null);
        if (user == null) {
            return null;
        }
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setBirthDate(LocalDate.parse(userDTO.getBirthDate()));
        user.setGender(EGender.valueOf(userDTO.getGender()));
        user.setPhotoUrl(userDTO.getPhotoUrl());
        userRepository.saveAndFlush(user);
        return UserDTO.builder()
                .id(user.getId())
                .credentialId(userCredential.getId())
                .email(userCredential.getEmail())
                .role(userCredential.getRole().getName().toString())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                // .gender(user.getGender().toString())
                // .address(user.getAddress())
                // .birthDate(user.getBirthDate().toString())
                // .photoUrl(user.getPhotoUrl())
                .isActive(user.getIsActive())
                .build();
    }

    @Override
    public String deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        user.setIsActive(false);
        userRepository.save(user);
        return user.getId();
    }
}
