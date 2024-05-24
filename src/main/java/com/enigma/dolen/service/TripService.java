package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.TripDTO;
import com.enigma.dolen.model.dto.TripRequest;
import com.enigma.dolen.model.dto.TripResponse;
import com.enigma.dolen.model.entity.Trip;

import java.util.List;

public interface TripService {
    Trip getTripByIdForOther(String id);
    TripResponse createTrip(String travel_id, TripRequest tripRequest);
    List<TripResponse> getTripByTravelId(String travelId);
    List<TripResponse> getAllTrip();
    TripResponse updateTrip(String tripId, TripRequest tripRequest);
    String deleteTrip(String tripId);
}
