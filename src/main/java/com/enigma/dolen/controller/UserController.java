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
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<UserDTO>builder()
                .message("User found")
                .statusCode(HttpStatus.OK.value())
                .data(userDTO)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<UserDTO>builder()
                .message("User updated")
                .statusCode(HttpStatus.OK.value())
                .data(updatedUser)
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<?>> deleteUser(@PathVariable String id) {
        UserCredential userCredential = userCredentialService.findById(id);
        String data = userService.deleteUser(userCredential.getUser().getId());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<String>builder()
                .message("User deleted")
                .statusCode(HttpStatus.OK.value())
                .data(data)
                .build()
        );
    }
}
