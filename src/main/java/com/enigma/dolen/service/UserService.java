package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.UserDto;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.request.UserRequest;
import com.enigma.dolen.model.response.UserResponse;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);
    UserDto getById(String id);
    List<UserDto> getAll();
    UserDto update(UserDto userDto);
    UserDto delete(String id);
    User getByIdForTravel(String id);
}
