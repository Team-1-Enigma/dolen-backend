package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItineraryDetailDTO {
    private String id;
    private String itineraryId;
    private String startTime;
    private String endTime;
    private String activityDesc;
}
