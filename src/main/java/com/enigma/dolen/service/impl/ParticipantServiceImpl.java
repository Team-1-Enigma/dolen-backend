package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.ParticipantDTO;
import com.enigma.dolen.model.entity.Participant;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.repository.ParticipantRepository;
import com.enigma.dolen.service.ParticipantService;
import com.enigma.dolen.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;
    private final TripService tripService;

    @Override
    public List<ParticipantDTO> getAllParticipantByTripId(String tripId) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);

        List<ParticipantDTO> participantDTOList = existingTrip.getParticipants().stream()
                .map(participant -> ParticipantDTO.builder()
                        .id(participant.getId())
                        .participantName(participant.getParticipantName())
                        .tripId(participant.getTrip().getId())
                        .contact(participant.getContact())
                        .build())
                .toList();

        return participantDTOList;
    }
}
