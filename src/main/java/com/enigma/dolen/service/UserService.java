package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;

public interface UserService {

    // UserDTO getUserById(String id);
    //
    // UserDTO getUserByEmail(String email);

    User createUser(UserDTO userDTO);

    // UserDTO updateUser(UserDTO userDTO);

    // UserDTO deleteUser(String id);
}
