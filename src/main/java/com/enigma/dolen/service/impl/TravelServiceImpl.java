package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.dto.TravelResponse;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.TravelRepository;
import com.enigma.dolen.service.RoleService;
import com.enigma.dolen.service.TravelService;
import com.enigma.dolen.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final RoleService roleService;
    private final UserService userService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TravelResponse createTravel(TravelDTO travelDto) {
        UserDTO existingUser = userService.getUserById(travelDto.getUserId());

        roleService.getOrSave(ERole.TRAVEL_OWNER);

        Travel travel = Travel.builder()
                .user(User.builder()
                        .id(existingUser.getId())
                        .fullName(existingUser.getFullName())
                        .phoneNumber(existingUser.getPhoneNumber())
                        .build())
                .name(travelDto.getName())
                .contactInfo(travelDto.getContactInfo())
                .address(travelDto.getAddress())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        travelRepository.saveAndFlush(travel);

        return toTravelResponse(travel);
    }

    private static TravelResponse toTravelResponse(Travel travel) {
        return TravelResponse.builder()
                .id(travel.getId())
                .userId(travel.getUser().getId())
                .name(travel.getName())
                .contactInfo(travel.getContactInfo())
                .address(travel.getAddress())
                .build();
    }

    @Override
    public Travel getTravelByIdForOther(String id) {
        return travelRepository.findById(id).orElse(null);
    }

    @Override
    public TravelResponse getTravelById(String id) {
        Travel travel = travelRepository.findById(id).orElse(null);
        return toTravelResponse(travel);
    }


    @Override
    public List<TravelResponse> getAllTravel() {
        List<Travel> travels = travelRepository.findAll();

        return travels.stream()
                .map(travel -> toTravelResponse(travel))
                .collect(Collectors.toList());
    }

    @Override
    public TravelResponse updateTravel(TravelDTO travelDto) {
        Travel existingTravel = travelRepository.findById(travelDto.getId()).orElse(null);
        if (existingTravel == null) {
            return null;
        }
        Travel saveTravel = Travel.builder()
                .id(existingTravel.getId())
                .user(existingTravel.getUser())
                .name(travelDto.getName())
                .contactInfo(travelDto.getContactInfo())
                .address(travelDto.getAddress())
                .createdAt(existingTravel.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isActive(existingTravel.getIsActive())
                .build();
        travelRepository.save(saveTravel);

        return toTravelResponse(saveTravel);
    }

    @Override
    public TravelResponse deleteTravel(String id) {
        Travel travelToDelete = travelRepository.findById(id).orElse(null);
        if (travelToDelete == null){
            return null;
        }
        travelToDelete.setIsActive(false);
        travelRepository.save(travelToDelete);
        return toTravelResponse(travelToDelete);
    }
}
