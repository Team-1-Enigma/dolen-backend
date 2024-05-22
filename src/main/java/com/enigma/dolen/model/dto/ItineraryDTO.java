package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.ItineraryDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItineraryDTO {

    private String id;
    private String travelId;
    private Integer dayNumber;
    private String description;
    private List<ItineraryDetail> itineraryDetails;
}
