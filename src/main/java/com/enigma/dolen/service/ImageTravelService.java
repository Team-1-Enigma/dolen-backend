package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.ImageTravelDTO;
import com.enigma.dolen.model.dto.ImageTravelResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.entity.ImageTravel;
import com.enigma.dolen.model.entity.Travel;

public interface ImageTravelService {
    ImageTravelResponse createImageTravel(TravelDTO TravelDTO, Travel travel);
    ImageTravelResponse addPhoto(ImageTravelDTO imageTravelDTO);
    ImageTravelResponse getImageTravelById(String id);
    ImageTravelResponse deleteImageTravel(String id);
}
