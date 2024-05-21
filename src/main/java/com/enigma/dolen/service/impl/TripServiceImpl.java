package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.repository.TripRepository;
import com.enigma.dolen.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    @Override
    public Trip getTripByIdForOther(String id) {
        return tripRepository.findById(id).orElse(null);
    }
}
