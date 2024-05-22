package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.ItineraryDTO;
import com.enigma.dolen.model.entity.Itinerary;
import com.enigma.dolen.model.entity.ItineraryDetail;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.repository.ItineraryDetailRepository;
import com.enigma.dolen.repository.ItineraryRepository;
import com.enigma.dolen.service.ItineraryDetailService;
import com.enigma.dolen.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final ItineraryDetailService itineraryDetailService;


    @Override
    public ItineraryDTO create(ItineraryDTO itineraryDTO) {

        List<ItineraryDetail> itineraryDetails = new ArrayList<>();
        for (ItineraryDetail itineraryDetail : itineraryDTO.getItineraryDetails()) {
            itineraryDetails.add(itineraryDetailService.create(itineraryDetail));
        }

        Itinerary itinerary = Itinerary.builder()
                .dayNumber(itineraryDTO.getDayNumber())
                .trip(null)
                .itineraryDetails(itineraryDetails)
                .build();

        Itinerary savedItinerary = itineraryRepository.saveAndFlush(itinerary);
        return toItineraryDTO(savedItinerary);
    }

    private ItineraryDTO toItineraryDTO(Itinerary savedItinerary) {
        return ItineraryDTO.builder()
                .id(savedItinerary.getId())
                .dayNumber(savedItinerary.getDayNumber())
                .itineraryDetails(savedItinerary.getItineraryDetails())
                .build();
    }

    @Override
    public ItineraryDTO findById(String id) {
        return null;
    }

    @Override
    public ItineraryDTO update(Integer day, String description) {
        return null;
    }

    @Override
    public String delete(String id) {
        return "";
    }
}
