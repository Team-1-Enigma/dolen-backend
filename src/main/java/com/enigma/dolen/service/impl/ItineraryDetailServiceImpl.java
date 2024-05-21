package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.entity.ItineraryDetail;
import com.enigma.dolen.model.exception.ApplicationException;
import com.enigma.dolen.repository.ItineraryDetailRepository;
import com.enigma.dolen.service.ItineraryDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItineraryDetailServiceImpl implements ItineraryDetailService {

    private final ItineraryDetailRepository itineraryDetailRepository;

    @Override
    public ItineraryDetail create(ItineraryDetail itineraryDetail) {
        return itineraryDetailRepository.save(itineraryDetail);
    }

    @Override
    public ItineraryDetail findById(String id) {
        return itineraryDetailRepository.findById(id).orElseThrow(() ->
                new ApplicationException("Itinerary Detail not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public ItineraryDetail update(ItineraryDetail itineraryDetail) {
        return itineraryDetailRepository.save(itineraryDetail);
    }

    @Override
    public String delete(String id) {
        if (!itineraryDetailRepository.existsById(id)) {
            throw new ApplicationException("Itinerary Detail not found", HttpStatus.NOT_FOUND);
        }
        itineraryDetailRepository.deleteById(id);
        return id;
    }
}
