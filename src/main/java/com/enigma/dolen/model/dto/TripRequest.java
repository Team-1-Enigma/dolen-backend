package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.Itinerary;
import com.enigma.dolen.model.entity.Participant;
import com.enigma.dolen.model.entity.TripPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TripRequest {
    private String id;
    private String travelId;
    private String destination;
    private String departureDate;
    private String returnDate;

    private LocationDTO locationDTO;

    private TripPriceRequest tripPriceRequest;
    private List<ItineraryDTO> itineraryDTOList;

    private List<MultipartFile> files;
}
