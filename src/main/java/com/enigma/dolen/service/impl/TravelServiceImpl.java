package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.EGender;
import com.enigma.dolen.constant.ERole;
import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.AppUser;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.repository.TravelRepository;
import com.enigma.dolen.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final UserCredentialService userCredentialService;

    @Lazy
    private ImageTravelService imageTravelService;

    @Lazy
    private BankAccountService bankAccountService;

    @Autowired
    @Lazy
    public void setImageTravelService(ImageTravelService imageTravelService) {
        this.imageTravelService = imageTravelService;
    }

    @Autowired
    @Lazy
    public void setBankAccountService(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TravelCreateResponse createTravel(TravelRequest travelRequest) {
        UserDTO existingUser = userService.getUserById(travelRequest.getUserId());
        AppUser userCredential = userCredentialService.loadUserById(existingUser.getCredentialId());
//        if(userCredential.getRole() == ERole.TRAVEL_OWNER){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "YOU ALREADY HAVE TRAVEL");
//        }
        userCredentialService.changeUserRole(ERole.TRAVEL_OWNER, existingUser.getCredentialId());

        Travel travel = Travel.builder()
                .user(User.builder()
                        .id(existingUser.getId())
                        .fullName(existingUser.getFullName())
                        .address(existingUser.getAddress())
                        .birthDate(Optional.ofNullable(existingUser.getBirthDate()).map(LocalDate::parse)
                        .orElse(null))
                        .phoneNumber(existingUser.getPhoneNumber())
                        .gender(Optional.ofNullable(existingUser.getGender()).map(EGender::valueOf).orElse(null))
                        .photoUrl(existingUser.getPhotoUrl())
                        .build())
                .name(travelRequest.getName())
                .contactInfo(travelRequest.getContactInfo())
                .address(travelRequest.getAddress())
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();
        travelRepository.saveAndFlush(travel);


        List<ImageTravelResponse> imageTravelResponses = imageTravelService.createImageTravel(travel, travelRequest);
        List<BankAccountResponse> bankAccountResponse = bankAccountService.createBankAccount(travel, travelRequest);

        return TravelCreateResponse.builder()
                .id(travel.getId())
                .userId(travel.getUser().getId())
                .name(travel.getName())
                .contactInfo(travel.getContactInfo())
                .address(travel.getAddress())
                .createdAt(travel.getCreatedAt())
                .isActive(travel.getIsActive())
                .bankAccountResponseList(null)
                .imageTravelResponseList(imageTravelResponses)
                .build();
    }

    private static TravelResponse toTravelResponse(Travel travel) {
        return TravelResponse.builder()
                .id(travel.getId())
                .userId(travel.getUser().getId())
                .name(travel.getName())
                .contactInfo(travel.getContactInfo())
                .address(travel.getAddress())
                .isActive(travel.getIsActive())
                .build();
    }

    @Override
    public Travel getTravelByIdForOther(String id) {
        return travelRepository.findById(id).orElse(null);
    }

    @Override
    public TravelCreateResponse getTravelById(String id) {
        Travel travel = travelRepository.findById(id).orElse(null);

        List<BankAccountResponse> bankAccountResponseList = travel.getBankAccounts().stream().map(
                bankAccount -> BankAccountResponse.builder()
                       .id(bankAccount.getId())
                       .bankName(bankAccount.getBankName())
                        .accountNumber(bankAccount.getAccountNumber())
                        .aliasName(bankAccount.getAliasName())
                        .isActive(bankAccount.getIsActive())
                        .createdAt(bankAccount.getCreatedAt())
                       .build()
        ).toList();

        List<ImageTravelResponse> imageTravelResponseList = travel.getImageTravels().stream().map(
                imageTravel -> ImageTravelResponse.builder()
                       .id(imageTravel.getId())
                       .imageUrl(imageTravel.getImageUrl())
                        .isActive(imageTravel.getIsActive())
                        .build()
                ).toList();

        return TravelCreateResponse.builder()
                .id(travel.getId())
                .userId(travel.getUser().getId())
                .name(travel.getName())
                .contactInfo(travel.getContactInfo())
                .address(travel.getAddress())
                .isActive(travel.getIsActive())
                .createdAt(travel.getCreatedAt())
                .bankAccountResponseList(bankAccountResponseList)
                .imageTravelResponseList(imageTravelResponseList)
                .build();
    }


    @Override
    public List<TravelCreateResponse> getAllTravel() {
        List<TravelCreateResponse> travelCreateResponses = new ArrayList<>();
        List<Travel> travels = travelRepository.findAll();

        for(Travel travel : travels ){
            if(travel.getIsActive() == true){
                List<ImageTravelResponse> imageTravels = travel.getImageTravels().stream()
                        .filter(imageTravel -> imageTravel.getIsActive())
                        .map(imageTravel -> ImageTravelResponse.builder()
                                .id(imageTravel.getId())
                                .imageUrl(imageTravel.getImageUrl())
                                .isActive(imageTravel.getIsActive())
                                .build())
                        .toList();


                List<BankAccountResponse> bankAccounts = travel.getBankAccounts().stream()
                        .filter(bankAccount -> bankAccount.getIsActive())
                        .map(bankAccount -> BankAccountResponse.builder()
                                .id(bankAccount.getId())
                                .name(bankAccount.getName())
                                .bankName(bankAccount.getBankName())
                                .accountNumber(bankAccount.getAccountNumber())
                                .aliasName(bankAccount.getAliasName())
                                .isActive(bankAccount.getIsActive())
                                .createdAt(bankAccount.getCreatedAt())
                                .build())
                        .toList();

                TravelCreateResponse travelCreateResponse = TravelCreateResponse.builder()
                        .id(travel.getId())
                        .userId(travel.getUser().getId())
                        .name(travel.getName())
                        .contactInfo(travel.getContactInfo())
                        .address(travel.getAddress())
                        .isActive(travel.getIsActive())
                        .createdAt(travel.getCreatedAt())
                        .updatedAt(travel.getUpdatedAt())
                        .bankAccountResponseList(bankAccounts)
                        .imageTravelResponseList(imageTravels)
                        .build();
                travelCreateResponses.add(travelCreateResponse);
            }
        }

        return travelCreateResponses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TravelResponse updateTravel(String travelId, TravelDTO travelDTO) {
        Travel existingTravel = travelRepository.findById(travelId).orElse(null);
        if (existingTravel == null) {
            return null;
        }
        Travel saveTravel = Travel.builder()
                .id(existingTravel.getId())
                .user(existingTravel.getUser())
                .name(travelDTO.getName())
                .contactInfo(travelDTO.getContactInfo())
                .address(travelDTO.getAddress())
                .createdAt(existingTravel.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .isActive(existingTravel.getIsActive())
                .build();
        travelRepository.save(saveTravel);

        return toTravelResponse(saveTravel);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String deleteTravel(String id) {
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
                BankAccountResponse response = bankAccountService.deleteBankAccountForTravel(bankAccount.getId());
                bankAccountResponses.add(response);
            });
        }

        return travelToDelete.getId();
    }

    @Override
    public TravelCreateResponse getTravelByUserId(String userId) {
        UserDTO user = userService.getUserById(userId);

        Travel travel = travelRepository.findTravelByUser(User.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .phoneNumber(user.getPhoneNumber())
                        .address(user.getAddress())
                        .photoUrl(user.getPhotoUrl())
                .build());

        return TravelCreateResponse.builder()
                .id(travel.getId())
                .userId(travel.getUser().getId())
                .name(travel.getName())
                .contactInfo(travel.getContactInfo())
                .address(travel.getAddress())
                .createdAt(travel.getCreatedAt())
                .isActive(travel.getIsActive())
                .bankAccountResponseList(travel.getBankAccounts().stream()
                        .filter(bankAccount -> bankAccount.getIsActive())
                        .map(bankAccount -> BankAccountResponse.builder()
                                .id(bankAccount.getId())
                                .name(bankAccount.getName())
                                .bankName(bankAccount.getBankName())
                                .accountNumber(bankAccount.getAccountNumber())
                                .aliasName(bankAccount.getAliasName())
                                .isActive(bankAccount.getIsActive())
                                .createdAt(bankAccount.getCreatedAt())
                                .build())
                        .toList())
                .imageTravelResponseList(travel.getImageTravels().stream()
                        .filter(imageTravel -> imageTravel.getIsActive())
                        .map(imageTravel -> ImageTravelResponse.builder()
                                .id(imageTravel.getId())
                                .imageUrl(imageTravel.getImageUrl())
                                .isActive(imageTravel.getIsActive())
                                .build()
                ).toList())
                .build();
    }
}
