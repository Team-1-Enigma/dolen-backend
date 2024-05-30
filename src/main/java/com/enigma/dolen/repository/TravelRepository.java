package com.enigma.dolen.repository;

import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelRepository extends JpaRepository<Travel, String> {
    Travel findTravelByUser(User user);
}
