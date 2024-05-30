package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.ItineraryDetailDTO;
import com.enigma.dolen.model.entity.Itinerary;
import com.enigma.dolen.model.entity.ItineraryDetail;
import com.enigma.dolen.model.exception.ApplicationException;
import com.enigma.dolen.repository.ItineraryDetailRepository;
import com.enigma.dolen.service.ItineraryDetailService;
import com.enigma.dolen.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ItineraryDetailServiceImpl implements ItineraryDetailService {

    private final ItineraryDetailRepository itineraryDetailRepository;
    private final ItineraryService itineraryService;

    @Override
    public ItineraryDetail create(Itinerary itinerary, ItineraryDetailDTO itineraryDetailDTO) {
        Itinerary existingItinerary = itineraryService.findById(itinerary.getId());
        if (existingItinerary == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        ItineraryDetail itineraryDetail = ItineraryDetail.builder()
                .itinerary(itinerary)
                .startTime(LocalTime.parse(itineraryDetailDTO.getStartTime(), formatter))
                .endTime(LocalTime.parse(itineraryDetailDTO.getEndTime(), formatter))
                .activityDesc(itineraryDetailDTO.getActivityDesc())
                .build();

        return itineraryDetailRepository.save(itineraryDetail);
    }

    @Override
    public ItineraryDetail findById(String id) {
        return itineraryDetailRepository.findById(id).orElseThrow(() ->
                new ApplicationException("Itinerary Detail not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public ItineraryDetailDTO update(String id, ItineraryDetailDTO itineraryDetailDTO) {
        Itinerary existingItinerary = itineraryService.findById(itineraryDetailDTO.getItineraryId());
        if (existingItinerary == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        ItineraryDetail itineraryDetail = ItineraryDetail.builder()
                .id(id)
                .itinerary(existingItinerary)
                .startTime(LocalTime.parse(itineraryDetailDTO.getStartTime(), formatter))
                .endTime(LocalTime.parse(itineraryDetailDTO.getEndTime(), formatter))
                .activityDesc(itineraryDetailDTO.getActivityDesc())
                .build();
        itineraryDetailRepository.save(itineraryDetail);

        return ItineraryDetailDTO.builder()
                .id(itineraryDetail.getId())
                .itineraryId(itineraryDetail.getItinerary().getId())
                .startTime(String.valueOf(itineraryDetail.getStartTime()))
                .endTime(String.valueOf(itineraryDetail.getEndTime()))
                .activityDesc(itineraryDetail.getActivityDesc())
                .build();
    }

    @Override
    public ItineraryDetail updateForItinerary(Itinerary itinerary, ItineraryDetailDTO itineraryDetailDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        ItineraryDetail itineraryDetail = ItineraryDetail.builder()
                .id(itineraryDetailDTO.getId())
                .itinerary(itinerary)
                .startTime(LocalTime.parse(itineraryDetailDTO.getStartTime(), formatter))
                .endTime(LocalTime.parse(itineraryDetailDTO.getEndTime(), formatter))
                .activityDesc(itineraryDetailDTO.getActivityDesc())
                .build();
        return itineraryDetailRepository.save(itineraryDetail);
    }

    @Override
    public ItineraryDetail updateForTrip(Itinerary itinerary, ItineraryDetailDTO itineraryDetailDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        ItineraryDetail itineraryDetail = ItineraryDetail.builder()
                .id(itinerary.getId())
                .itinerary(itinerary)
                .startTime(LocalTime.parse(itineraryDetailDTO.getStartTime(), formatter))
                .endTime(LocalTime.parse(itineraryDetailDTO.getEndTime(), formatter))
                .activityDesc(itineraryDetailDTO.getActivityDesc())
                .build();
        itineraryDetailRepository.save(itineraryDetail);

        return itineraryDetail;
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
