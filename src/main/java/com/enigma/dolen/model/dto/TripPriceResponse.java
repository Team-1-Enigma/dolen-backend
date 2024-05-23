package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TripPriceResponse {
    private String id;
    private String tripId;
    private Long price;
    private Integer quota;
    private LocalDateTime createdAt;
    private Boolean isActive;
}
