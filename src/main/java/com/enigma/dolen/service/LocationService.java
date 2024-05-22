package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.LocationDTO;
import com.enigma.dolen.model.dto.TripRequest;
import com.enigma.dolen.model.entity.Location;

import java.util.List;

public interface LocationService {
    Location getOrSave(LocationDTO locationDTO);
}