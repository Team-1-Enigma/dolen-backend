package com.enigma.dolen.repository;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.entity.Location;
import com.enigma.dolen.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {
    Optional<Location> findByCity(String city);
}
