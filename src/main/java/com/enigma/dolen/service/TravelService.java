package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Travel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TravelService {
    TravelCreateResponse createTravel(TravelRequest travelRequest);
    Travel getTravelByIdForOther(String id);
    TravelCreateResponse getTravelById(String id);
    List<TravelCreateResponse> getAllTravel();
    TravelResponse updateTravel(String travelId, TravelDTO travelDTO);
    String deleteTravel(String id);

    TravelResponse getTravelByUserId(String userId);
}
