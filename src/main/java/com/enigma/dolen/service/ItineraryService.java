package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.ItineraryDTO;
import com.enigma.dolen.model.entity.Trip;

public interface ItineraryService {

    ItineraryDTO create(ItineraryDTO itineraryDTO);

    ItineraryDTO findById(String id);

    ItineraryDTO update(Integer day, String description);

    String delete(String id);
}
