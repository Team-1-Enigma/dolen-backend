package com.enigma.dolen.service;

import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.entity.Status;

import java.util.List;

public interface StatusService {
    Status createStatus(Status status);
    Status getStatus(String orderId);
    List<Status> getStatusByTravelId(String travelId, String status);
}
