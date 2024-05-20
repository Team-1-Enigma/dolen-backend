package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.EGender;
import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.dto.BankAccountResponse;
import com.enigma.dolen.model.dto.ImageTravelResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.dto.TravelResponse;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.TravelRepository;
import com.enigma.dolen.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.util.ClassUtil.name;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final ImageTravelService imageTravelService;
    private final BankAccountService bankAccountService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TravelResponse createTravel(TravelDTO travelDto) {
        UserDTO existingUser = userService.getUserById(travelDto.getUserId());

        roleService.getOrSave(ERole.TRAVEL_OWNER);

        Travel travel = Travel.builder()
                .user(User.builder()
                        .id(existingUser.getId())
                        .fullName(existingUser.getFullName())
                        .address(existingUser.getAddress())
                        .birthDate(LocalDate.parse(existingUser.getBirthDate()))
                        .phoneNumber(existingUser.getPhoneNumber())
                        .gender(EGender.valueOf(existingUser.getGender()))
                        .photoUrl(existingUser.getPhotoUrl())
                        .build())
                .name(travelDto.getName())
                .contactInfo(travelDto.getContactInfo())
                .address(travelDto.getAddress())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        travelRepository.saveAndFlush(travel);

        ImageTravelResponse imageTravelResponse = imageTravelService.createImageTravel(travelDto, travel);
        BankAccountResponse bankAccountResponse = bankAccountService.createBankAccount(travelDto, travel);

        return toTravelResponse(travel, imageTravelResponse, bankAccountResponse);
    }

    private static TravelResponse toTravelResponse(Travel travel, ImageTravelResponse imageTravelResponse, BankAccountResponse bankAccountResponse) {
        return TravelResponse.builder()
                .id(travel.getId())
                .userId(travel.getUser().getId())
                .name(travel.getName())
                .contactInfo(travel.getContactInfo())
                .address(travel.getAddress())
                .bankAccountResponse(bankAccountResponse)
                .imageTravelResponse(imageTravelResponse)
                .build();
    }

    @Override
    public Travel getTravelByIdForOther(String id) {
        return travelRepository.findById(id).orElse(null);
    }

    @Override
    public TravelResponse getTravelById(String id) {
        Travel travel = travelRepository.findById(id).orElse(null);
        return toTravelResponse(travel, null, null);
    }


    @Override
    public List<TravelResponse> getAllTravel() {
        List<Travel> travels = travelRepository.findAll();

        return travels.stream()
                .map(travel -> toTravelResponse(travel,null,null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
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

        return toTravelResponse(saveTravel, null,null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TravelResponse deleteTravel(String id) {
        Travel travelToDelete = travelRepository.findById(id).orElse(null);
        if (travelToDelete == null){
            return null;
        }
        travelToDelete.setIsActive(false);
        travelRepository.save(travelToDelete);

        List<ImageTravelResponse> imageTravelResponses = new ArrayList<>();
        if (travelToDelete.getImageTravels() != null) {
            travelToDelete.getImageTravels().forEach(imageTravel -> {
                ImageTravelResponse response = imageTravelService.deleteImageTravel(imageTravel.getId());
                imageTravelResponses.add(response);
            });
        }

        List<BankAccountResponse> bankAccountResponses = new ArrayList<>();
        if (travelToDelete.getBankAccounts() != null) {
            travelToDelete.getBankAccounts().forEach(bankAccount -> {
                BankAccountResponse response = bankAccountService.deleteBankAccount(bankAccount.getId());
                bankAccountResponses.add(response);
            });
        }

        return toTravelResponse(travelToDelete, null, null);
    }
}
