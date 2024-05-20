package com.enigma.dolen.service.impl;

import java.time.LocalDate;
import java.util.Optional;

import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.model.exception.ApplicationException;
import com.enigma.dolen.service.UploadImageService;
import com.enigma.dolen.service.UserCredentialService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.enigma.dolen.constant.EGender;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.UserRepository;
import com.enigma.dolen.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCredentialService userCredentialService;
    private final UploadImageService uploadImageService;

    @Override
    public UserDTO getUserById(String id) {
        UserCredential userCredential = userCredentialService.findById(id);
        User user = userRepository.findById(userCredential.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User not found", HttpStatus.NOT_FOUND));
        return getUserDTO(user, userCredential);
    }

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

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        UserCredential userCredential = userCredentialService.findById(id);
        User user = userRepository.findById(userCredential.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User not found", HttpStatus.NOT_FOUND));
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setBirthDate(LocalDate.parse(userDTO.getBirthDate()));
        user.setGender(EGender.valueOf(userDTO.getGender()));
        userRepository.saveAndFlush(user);
        return getUserDTO(user, userCredential);
    }

    @Override
    public String deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("User not found", HttpStatus.NOT_FOUND));
        user.setIsActive(false);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public String uploadPhoto(MultipartFile file, String id) {
        UserCredential userCredential = userCredentialService.findById(id);
        User user = userRepository.findById(userCredential.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User not found", HttpStatus.NOT_FOUND));
        String photoUrl = uploadImageService.uploadImage(file);
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);
        return photoUrl;
    }

    private static UserDTO getUserDTO(User user, UserCredential userCredential) {
        return UserDTO.builder()
                .id(user.getId())
                .credentialId(userCredential.getId())
                .email(userCredential.getEmail())
                .role(userCredential.getRole().getName().toString())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .gender(Optional.ofNullable(user.getGender()).map(Enum::toString).orElse(null))
                .address(user.getAddress())
                .birthDate(Optional.ofNullable(user.getBirthDate()).map(Object::toString).orElse(null))
                .photoUrl(user.getPhotoUrl())
                .isActive(user.getIsActive())
                .build();
    }
}
