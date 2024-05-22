package com.enigma.dolen.service;

import com.enigma.dolen.model.entity.ItineraryDetail;

public interface ItineraryDetailService {

    ItineraryDetail create(ItineraryDetail itineraryDetail);

    ItineraryDetail findById(String id);

    ItineraryDetail update(ItineraryDetail itineraryDetail);

    String delete(String id);
}
