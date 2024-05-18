package com.enigma.dolen.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String fullName;
    private String phoneNumber;
    private String gender;
    private LocalDate birthDate;
    private String address;
    private String photoUrl;
}
