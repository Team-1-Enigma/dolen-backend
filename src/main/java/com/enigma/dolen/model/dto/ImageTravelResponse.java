package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageTravelResponse {
    private String id;
    private String imageUrl;
    private Boolean isActive;

    private TravelDTO travelDTO;
}
