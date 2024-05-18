package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;

public interface UserService {

    User createUser(UserDTO userDTO);

    UserDTO getUserById(String id);

    UserDTO updateUser(String id, UserDTO userDTO);

    String deleteUser(String id);
}
