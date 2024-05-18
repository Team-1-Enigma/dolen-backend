package com.enigma.dolen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.service.UserCredentialService;
import com.enigma.dolen.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserCredentialService userCredentialService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> getUserById(@PathVariable String id) {
        UserCredential userCredential = userCredentialService.findById(id);
        User user = userService.getUserById(userCredential.getUser().getId());
        UserDTO userDTO = UserDTO.builder()
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

        CommonResponse<UserDTO> response = CommonResponse.<UserDTO>builder()
                .message("User found")
                .statusCode(HttpStatus.OK.value())
                .data(userDTO)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserCredential userCredential = userCredentialService.findById(id);
        User user = userService.updateUser(userCredential.getUser().getId(), userDTO);

        UserDTO updatedUser = UserDTO.builder()
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

        CommonResponse<UserDTO> response = CommonResponse.<UserDTO>builder()
                .message("User updated")
                .statusCode(HttpStatus.OK.value())
                .data(updatedUser)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> deleteUser(@PathVariable String id) {
        UserCredential userCredential = userCredentialService.findById(id);
        userService.deleteUser(userCredential.getUser().getId());
        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("User deleted")
                .statusCode(HttpStatus.OK.value())
                .data(userCredential.getId())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
