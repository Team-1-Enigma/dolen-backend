package com.enigma.dolen.service.impl;

import org.springframework.stereotype.Service;

import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.UserRepository;
import com.enigma.dolen.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // @Override
    // public UserDTO getUserById(String id) {
    // }

    // @Override
    // public UserDTO getUserByEmail(String email) {
    // }

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

    // @Override
    // public UserDTO updateUser(UserDTO userDTO) {
    // }

    // @Override
    // public UserDTO deleteUser(String id) {
    // }

}
