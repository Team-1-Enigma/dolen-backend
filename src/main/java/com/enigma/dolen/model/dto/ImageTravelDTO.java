package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.Travel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageTravelDTO {
    private String id;
    private String imageUrl;
    private Boolean isActive;

    private String travelId;
}
