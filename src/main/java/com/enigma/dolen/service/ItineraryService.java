package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.ItineraryDTO;
import com.enigma.dolen.model.dto.TripRequest;
import com.enigma.dolen.model.entity.Itinerary;
import com.enigma.dolen.model.entity.Trip;

import java.util.List;

public interface ItineraryService {

    List<ItineraryDTO> create(Trip trip, TripRequest tripRequest);

    ItineraryDTO addItinerary(ItineraryDTO itineraryDTO);

    Itinerary findById(String id);

    List<ItineraryDTO> getAllItineraryByTripId(String tripId);

    ItineraryDTO update(String id, ItineraryDTO itineraryDTO);

    List<ItineraryDTO> updateForTrip(Trip trip, TripRequest tripRequest);

    String delete(String id);
}
