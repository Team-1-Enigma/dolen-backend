package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Travel;

import java.util.List;

public interface ImageTravelService {
    List<ImageTravelResponse> createImageTravel(Travel travel, TravelRequest travelRequest);
    List<ImageTravelResponse> addPhoto(AddPhotoRequest addPhotoRequest);
    List<ImageTravelResponse> getAllPhotoByTravelId(String travelId);
    ImageTravelResponse getImageTravelById(String id);
    ImageTravelResponse deleteImageTravel(String id);
}
