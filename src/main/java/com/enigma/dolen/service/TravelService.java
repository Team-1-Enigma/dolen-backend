package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.dto.TravelResponse;
import com.enigma.dolen.model.entity.Travel;

import java.util.List;

public interface TravelService {
    TravelResponse createTravel(TravelDTO travelDto);
    Travel getTravelByIdForOther(String id);
    TravelResponse getTravelById(String id);
    List<TravelResponse> getAllTravel();
    TravelResponse updateTravel(TravelDTO travelDto);
    TravelResponse deleteTravel(String id);
}
