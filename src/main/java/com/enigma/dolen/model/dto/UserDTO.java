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
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String gender;
    private String address;
    private String photoUrl;
    private Boolean isActive;
    private String role;
}
