package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.ItineraryDetailDTO;
import com.enigma.dolen.model.entity.Itinerary;
import com.enigma.dolen.model.entity.ItineraryDetail;

public interface ItineraryDetailService {

    ItineraryDetail create(Itinerary itinerary, ItineraryDetailDTO itineraryDetailDTO);

    ItineraryDetail findById(String id);

    ItineraryDetailDTO update(String id, ItineraryDetailDTO itineraryDetailDTO);

    ItineraryDetail updateForTrip(Itinerary itinerary, ItineraryDetailDTO itineraryDetailDTO);

    String delete(String id);
}
