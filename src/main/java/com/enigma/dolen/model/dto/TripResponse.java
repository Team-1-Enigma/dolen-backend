package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.Travel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TripResponse {
    private String id;
    private String travelId;
    private String destination;
    private String departureDate;
    private String returnDate;

    private LocationDTO locationDTO;

    private TripPriceDiscountResponse tripPriceDiscountResponse;

    private List<ImageTripResponse> imageTripResponseList;
    private TripPriceResponse tripPriceResponse;
    private List<ItineraryDTO> itineraryDTOList;
    private TravelDTO travelDTO;

}
