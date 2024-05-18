package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String credentialId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String gender;
    private String birthDate;
    private String address;
    private String photoUrl;
    private String role;
    private Boolean isActive;
}