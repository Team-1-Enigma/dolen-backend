package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.Gender;
import com.enigma.dolen.model.dto.UserDto;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.UserRepository;
import com.enigma.dolen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = User.builder()
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .gender(Gender.valueOf(userDto.getGender()))
                .birthDate(userDto.getBirthDate())
                .address(userDto.getAddress())
                .photoUrl(userDto.getPhotoUrl())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        userRepository.save(user);

        return UserDto.builder()
                .fullName(user.getFullName())
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .gender(String.valueOf(user.getGender()))
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .photoUrl(user.getPhotoUrl())
                .build();
    }

    @Override
    public UserDto getById(String id) {
        return null;
    }

    @Override
    public List<UserDto> getAll() {
        return List.of();
    }

    @Override
    public UserDto update(UserDto userRequest) {
        return null;
    }

    @Override
    public UserDto delete(String id) {
        return null;
    }

    @Override
    public User getByIdForTravel(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
