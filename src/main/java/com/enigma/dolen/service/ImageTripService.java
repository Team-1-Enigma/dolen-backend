package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.model.entity.Trip;

import java.util.List;

public interface ImageTripService {
    List<ImageTripResponse> createImageTrip(Trip trip, TripRequest tripRequest);
    List<ImageTripResponse> addPhoto(AddPhotoTripRequest addPhotoTripRequest);
    List<ImageTripResponse> getAllPhotoByTravelId(String tripId);
    List<ImageTripResponse> updateImageTrip(Trip trip, TripRequest tripRequest);
    ImageTripResponse deleteImageTrip(String id);
}
