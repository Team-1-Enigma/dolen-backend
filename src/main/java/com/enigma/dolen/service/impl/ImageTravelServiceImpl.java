package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.FeedbackResponse;
import com.enigma.dolen.model.dto.ImageTravelDTO;
import com.enigma.dolen.model.dto.ImageTravelResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.entity.Feedback;
import com.enigma.dolen.model.entity.ImageTravel;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.repository.ImageTravelRepository;
import com.enigma.dolen.service.ImageTravelService;
import com.enigma.dolen.service.TravelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageTravelServiceImpl implements ImageTravelService {

    private final ImageTravelRepository imageTravelRepository;
    private final TravelService travelService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ImageTravelResponse createImageTravel(ImageTravelDTO imageTravelDTO) {

        Travel travel = travelService.getTravelByIdForOther(imageTravelDTO.getTravelId());

        ImageTravel imageTravel = ImageTravel.builder()
                .travel(travel)
                .imageUrl(imageTravelDTO.getImageUrl())
                .isActive(true)
                .build();
        imageTravelRepository.saveAndFlush(imageTravel);

        return toImageTravelResponse(imageTravel);
    }

    private static ImageTravelResponse toImageTravelResponse(ImageTravel imageTravel) {
        ImageTravelResponse imageTravelResponse = ImageTravelResponse.builder()
                .id(imageTravel.getId())
                .travelDTO(TravelDTO.builder()
                        .id(imageTravel.getTravel().getId())
                        .userId(imageTravel.getTravel().getUser().getId())
                        .name(imageTravel.getTravel().getName())
                        .contactInfo(imageTravel.getTravel().getContactInfo())
                        .address(imageTravel.getTravel().getAddress())
                        .build())
                .imageUrl(imageTravel.getImageUrl())
                .build();
        return imageTravelResponse;
    }

    @Override
    public ImageTravelResponse getImageTravelById(String id) {
        ImageTravel imageTravel = imageTravelRepository.findById(id).orElse(null);

        return toImageTravelResponse(imageTravel);
    }

    @Override
    public ImageTravelResponse deleteImageTravel(String id) {
        ImageTravel imageTravelToDelete = imageTravelRepository.findById(id).orElse(null);
        if (imageTravelToDelete == null){
            return null;
        }
        imageTravelToDelete.setIsActive(false);
        imageTravelRepository.save(imageTravelToDelete);
        return toImageTravelResponse(imageTravelToDelete);
    }
}
