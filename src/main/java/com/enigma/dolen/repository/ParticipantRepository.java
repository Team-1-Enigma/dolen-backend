package com.enigma.dolen.repository;

import com.enigma.dolen.model.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, String> {
}
