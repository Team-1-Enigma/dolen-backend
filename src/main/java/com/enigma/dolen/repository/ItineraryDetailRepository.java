package com.enigma.dolen.repository;

import com.enigma.dolen.model.entity.ItineraryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryDetailRepository extends JpaRepository<ItineraryDetail, String> {
}
