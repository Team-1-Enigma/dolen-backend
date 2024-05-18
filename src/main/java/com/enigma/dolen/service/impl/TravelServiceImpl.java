package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.TravelDto;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.TravelRepository;
import com.enigma.dolen.service.TravelService;
import com.enigma.dolen.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

    private final UserService userService;
    private final TravelRepository travelRepository;

    @Override
    public TravelDto create(TravelDto travelDto) {
        User existingUser = userService.getByIdForTravel(travelDto.getUserId());

        Travel travel = Travel.builder()
                .user(existingUser)
                .name(travelDto.getName())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        travelRepository.save(travel);

        return toTravelDto(existingUser, travel);
    }

    private static TravelDto toTravelDto(User existingUser, Travel travel) {
        return TravelDto.builder()
                .userFullname(existingUser.getFullName())
                .name(travel.getName())
                .build();
    }

    @Override
    public TravelDto getById(String id) {
        Travel existingTravel = travelRepository.findById(id).orElse(null);
        User user = userService.getByIdForTravel(existingTravel.getUser().getId());

        return toTravelDto(user, existingTravel);
    }

    @Override
    public List<TravelDto> getAll() {
        List<Travel> travels = travelRepository.findAll();

        return travels.stream()
                .map(travel -> toTravelDto(userService.getByIdForTravel(travel.getUser().getId()), travel))
                .collect(Collectors.toList());
    }

    @Override
    public TravelDto update(TravelDto travelDto) {
        return null;
    }

    @Override
    public TravelDto delete(String id) {
        return null;
    }
}
