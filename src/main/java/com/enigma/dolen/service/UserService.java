package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;

public interface UserService {

    User getUserById(String id);

    User createUser(UserDTO userDTO);

    User updateUser(UserDTO userDTO);

    User deleteUser(String id);
}
