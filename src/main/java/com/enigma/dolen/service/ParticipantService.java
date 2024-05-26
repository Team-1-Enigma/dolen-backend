package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.ParticipantDTO;

import java.util.List;

public interface ParticipantService {
    List<ParticipantDTO> getAllParticipantByTripId(String tripId);
    ParticipantDTO createParticipant(ParticipantDTO participantDTO);
}
