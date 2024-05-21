package com.enigma.dolen.service;

import com.enigma.dolen.model.entity.Trip;

public interface TripService {
    Trip getTripByIdForOther(String id);
}
