package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.dto.TravelCreateResponse;
import com.enigma.dolen.model.entity.Order;
import com.enigma.dolen.model.entity.Status;
import com.enigma.dolen.repository.OrderRepository;
import com.enigma.dolen.repository.StatusRepository;
import com.enigma.dolen.service.StatusService;
import com.enigma.dolen.service.TravelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;
    private final TravelService travelService;
    private final OrderRepository orderRepository;
    @Override
    public Status createStatus(Status status) {
        status.setStatus(EStatus.valueOf("WAITING"));
        return statusRepository.save(status);
    }

    @Override
    public Status getStatus(String orderId) {
        return statusRepository.getStatusByOrder_Id(orderId);
    }

    @Override
    public List<Status> getStatusByTravelId(String travelId, String status) {
        TravelCreateResponse travel = travelService.getTravelById(travelId);
        //GET ALL ORDER IN TRAVEL

        //STREAM ALL ORDER AND GET BY STATUS
        return null;
    }

    @Override
    public Status changeStatus(EStatus eStatus, String orderId) {

        Status status = statusRepository.getStatusByOrder_Id(orderId);
        status.setStatus(eStatus);

        return statusRepository.save(status);
    }
}
