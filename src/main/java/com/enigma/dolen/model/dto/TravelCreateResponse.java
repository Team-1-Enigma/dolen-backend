package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TravelCreateResponse {
    private String id;
    private String userId;
    private String name;
    private String contactInfo;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;

    private List<BankAccountResponse> bankAccountResponseList;
    private List<ImageTravelResponse> imageTravelResponseList;
}
