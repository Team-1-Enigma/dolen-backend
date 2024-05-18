package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.ImageTravelDTO;
import com.enigma.dolen.model.dto.ImageTravelResponse;
import com.enigma.dolen.model.entity.ImageTravel;

public interface ImageTravelService {
    ImageTravelResponse createImageTravel(ImageTravelDTO imageTravelDTO);
    ImageTravelResponse getImageTravelById(String id);
    ImageTravelResponse deleteImageTravel(String id);
}
