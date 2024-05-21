package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Feedback;
import com.enigma.dolen.model.entity.ImageTravel;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.repository.ImageTravelRepository;
import com.enigma.dolen.service.ImageTravelService;
import com.enigma.dolen.service.TravelService;
import com.enigma.dolen.service.UploadImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageTravelServiceImpl implements ImageTravelService {

    private final ImageTravelRepository imageTravelRepository;
    private final UploadImageService uploadImageService;
    private TravelService travelService;

    @Autowired
    @Lazy
    public void setTravelService(TravelService travelService) {
        this.travelService = travelService;
    }


    @Override
    public List<ImageTravelResponse> createImageTravel(Travel travel, TravelRequest travelRequest) {

        Travel existingTravel = travelService.getTravelByIdForOther(travel.getId());

        List<ImageTravelResponse> imageTravelUrls = new ArrayList<>();
        for(MultipartFile file : travelRequest.getFiles()) {
            String imageUrl = uploadImageService.uploadImage(file);

            ImageTravel imageTravel = ImageTravel.builder()
                    .travel(existingTravel)
                    .imageUrl(imageUrl)
                    .isActive(true)
                    .build();
            imageTravelRepository.saveAndFlush(imageTravel);

            imageTravelUrls.add(toImageTravelResponse(imageTravel));
        }


        return imageTravelUrls;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<ImageTravelResponse> addPhoto(AddPhotoRequest request) {
        Travel travel = travelService.getTravelByIdForOther(request.getTravel_id());

        List<ImageTravelResponse> imageTravelResponses = new ArrayList<>();
        for(MultipartFile file : request.getFiles()) {
            String imageUrl = uploadImageService.uploadImage(file);

            ImageTravel imageTravel = ImageTravel.builder()
                    .travel(travel)
                    .imageUrl(imageUrl)
                    .isActive(true)
                    .build();
            imageTravelRepository.saveAndFlush(imageTravel);

            imageTravelResponses.add(toImageTravelResponse(imageTravel));
        }


        return imageTravelResponses;
    }

    @Override
    public List<ImageTravelResponse> getAllPhotoByTravelId(String travelId) {
        Travel travel = travelService.getTravelByIdForOther(travelId);
        if (travel == null){
            return null;
        }
        List<ImageTravelResponse> travelUrls = travel.getImageTravels().stream()
                .filter(image -> image.getIsActive())
                .map(imageTravel -> ImageTravelResponse.builder()
                        .id(imageTravel.getId())
                        .imageUrl(imageTravel.getImageUrl())
                        .isActive(imageTravel.getIsActive())
                        .build())
                .toList();

        return travelUrls;
    }


    private static ImageTravelResponse toImageTravelResponse(ImageTravel imageTravel) {
        ImageTravelResponse imageTravelResponse = ImageTravelResponse.builder()
                .id(imageTravel.getId())
                .imageUrl(imageTravel.getImageUrl())
                .isActive(imageTravel.getIsActive())
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
