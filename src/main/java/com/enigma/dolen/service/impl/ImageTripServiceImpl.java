package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.AddPhotoTripRequest;
import com.enigma.dolen.model.dto.ImageTravelResponse;
import com.enigma.dolen.model.dto.ImageTripResponse;
import com.enigma.dolen.model.dto.TripRequest;
import com.enigma.dolen.model.entity.ImageTravel;
import com.enigma.dolen.model.entity.ImageTrip;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.repository.ImageTripRepository;
import com.enigma.dolen.service.ImageTripService;
import com.enigma.dolen.service.TripService;
import com.enigma.dolen.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageTripServiceImpl implements ImageTripService {

    private final ImageTripRepository imageTripRepository;
    private final UploadImageService uploadImageService;
    private TripService tripService;

    @Autowired
    @Lazy
    public void setTripService(TripService tripService) {
        this.tripService = tripService;
    }
    @Override
    public List<ImageTripResponse> createImageTrip(Trip trip, TripRequest tripRequest) {

        Trip existingTrip = tripService.getTripByIdForOther(trip.getId());

        List<ImageTripResponse> imageTripUrls = new ArrayList<>();
        for(MultipartFile file : tripRequest.getFiles()){
            String imageUrl = uploadImageService.uploadImage(file);

            ImageTrip imageTrip = ImageTrip.builder()
                    .trip(existingTrip)
                    .imageUrl(imageUrl)
                    .isActive(true)
                    .build();
            imageTripRepository.saveAndFlush(imageTrip);

            imageTripUrls.add(toImageTripResponse(imageTrip));
        }
        return imageTripUrls;
    }

    private static ImageTripResponse toImageTripResponse(ImageTrip imageTrip) {
        ImageTripResponse imageTripResponse = ImageTripResponse.builder()
                .id(imageTrip.getId())
                .imageUrl(imageTrip.getImageUrl())
                .isActive(imageTrip.getIsActive())
                .build();
        return imageTripResponse;
    }

    @Override
    public List<ImageTripResponse> addPhoto(AddPhotoTripRequest request) {
        Trip existingTrip = tripService.getTripByIdForOther(request.getTrip_id());

        List<ImageTripResponse> imageTripResponses = new ArrayList<>();
        for(MultipartFile file : request.getFiles()) {
            String imageUrl = uploadImageService.uploadImage(file);

            ImageTrip imageTrip = ImageTrip.builder()
                   .trip(existingTrip)
                   .imageUrl(imageUrl)
                   .isActive(true)
                   .build();
            imageTripRepository.saveAndFlush(imageTrip);

            imageTripResponses.add(toImageTripResponse(imageTrip));
        }
        return imageTripResponses;
    }



    @Override
    public List<ImageTripResponse> getAllPhotoByTravelId(String tripId) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);
        if (existingTrip == null){
            return null;
        }
        List<ImageTripResponse> imageTripResponses = existingTrip.getImageTrips().stream()
                .filter(imageTrip -> imageTrip.getIsActive())
               .map(imageTrip -> toImageTripResponse(imageTrip)).toList();
        return imageTripResponses;
    }

    @Override
    public List<ImageTripResponse> updateImageTrip(Trip trip, TripRequest tripRequest) {
        Trip existingTrip = tripService.getTripByIdForOther(trip.getId());

        for (ImageTrip imageTrip : existingTrip.getImageTrips()){
            if (imageTrip.getIsActive()){
                ImageTrip  value = ImageTrip.builder()
                        .id(imageTrip.getId())
                        .trip(existingTrip)
                        .imageUrl(imageTrip.getImageUrl())
                        .isActive(false)
                        .build();
                imageTripRepository.saveAndFlush(value);
            }
        }

        List<ImageTripResponse> imageTripUrls = new ArrayList<>();
        for(MultipartFile file : tripRequest.getFiles()){
            String imageUrl = uploadImageService.uploadImage(file);

            ImageTrip imageTrip = ImageTrip.builder()
                    .trip(existingTrip)
                    .imageUrl(imageUrl)
                    .isActive(true)
                    .build();
            imageTripRepository.saveAndFlush(imageTrip);

            imageTripUrls.add(toImageTripResponse(imageTrip));
        }
        return imageTripUrls;
    }

    @Override
    public ImageTripResponse deleteImageTrip(String id) {
        ImageTrip existingImageTrip = imageTripRepository.findById(id).orElse(null);
        if (existingImageTrip == null){
            return null;
        }
        existingImageTrip.setIsActive(false);
        imageTripRepository.save(existingImageTrip);
        return toImageTripResponse(existingImageTrip);
    }
}
