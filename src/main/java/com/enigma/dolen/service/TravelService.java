package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.TravelDto;

import java.util.List;

public interface TravelService {
    TravelDto create(TravelDto travelDto);
    TravelDto getById(String id);
    List<TravelDto> getAll();
    TravelDto update(TravelDto travelDto);
    TravelDto delete(String id);
}
