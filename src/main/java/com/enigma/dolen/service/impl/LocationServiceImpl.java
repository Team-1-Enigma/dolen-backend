package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.LocationDTO;
import com.enigma.dolen.model.entity.Location;
import com.enigma.dolen.repository.LocationRepository;
import com.enigma.dolen.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    @Override
    public Location getOrSave(LocationDTO locationDTO) {
        Optional<Location> optionalLocation = locationRepository.findByCity(locationDTO.getCity());
        if (optionalLocation.isPresent()) {
            return optionalLocation.get();
        }

        Location location = Location.builder()
                .city(locationDTO.getCity())
                .province(locationDTO.getProvince())
                .build();
        locationRepository.save(location);

        return Location.builder()
                .id(location.getId())
                .city(location.getCity())
                .province(location.getProvince())
                .build();
    }
}
