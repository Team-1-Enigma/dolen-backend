package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.UserDTO;

public interface UserService {

    UserDTO getUserById(String id);

    UserDTO getUserByEmail(String email);

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO deleteUser(String id);
}
