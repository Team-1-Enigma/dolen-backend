package com.enigma.dolen.service.impl;

import java.time.LocalDate;

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

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
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
    public User updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
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
        return user;
    }

    @Override
    public User deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        user.setIsActive(false);
        userRepository.save(user);
        return user;
    }
}
