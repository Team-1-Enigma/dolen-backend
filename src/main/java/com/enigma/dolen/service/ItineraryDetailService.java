package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.ItineraryDetailDTO;
import com.enigma.dolen.model.entity.ItineraryDetail;

public interface ItineraryDetailService {

    ItineraryDetail create(ItineraryDetail itineraryDetail);

    ItineraryDetail findById(String id);

    ItineraryDetailDTO update(String id, ItineraryDetailDTO itineraryDetailDTO);

    String delete(String id);
}
