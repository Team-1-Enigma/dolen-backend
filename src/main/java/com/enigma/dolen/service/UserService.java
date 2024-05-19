package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User createUser(UserDTO userDTO);

    UserDTO getUserById(String id);

    UserDTO updateUser(String id, UserDTO userDTO);

    String deleteUser(String id);

    String uploadImage(String id, MultipartFile file);
}
