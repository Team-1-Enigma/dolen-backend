package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.EGender;
import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.*;
import com.enigma.dolen.repository.OrderRepository;
import com.enigma.dolen.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final StatusService statusService;
    private final TripService tripService;
    private final TripPriceService tripPriceService;
    @Override
    @Transactional(rollbackOn = Exception.class)
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        //VALIDATE USER
        UserDTO userDto= userService.getUserById(orderRequest.getUserId());
        User user = User.builder()
                .id(userDto.getId())
                .fullName(userDto.getFullName())
                .photoUrl(userDto.getPhotoUrl())
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        List<OrderDetail> orderDetailList = orderRequest.getOrderDetailRequests().stream()
                .map((orderDetailRequest -> {
                    return OrderDetail.builder()
                            .participantName(orderDetailRequest.getParticipantName())
                            .contact(orderDetailRequest.getContact())
                            .build();
                })
        ).toList();

        //TODO:GET ORDER
        Trip trip = tripService.getTripByIdForOther(orderRequest.getTripId());

        //CREATE ORDER
        Order order = orderRepository.saveAndFlush(Order.builder()
                        .orderDetailList(orderDetailList)
                        .user(user)
                        .trip(trip)
                        .quantity(orderRequest.getQuantity())
                .build()
        );

        //TODO:REDUCE OPEN TRIP AVAILABLE QUOTA
        tripPriceService.reduceTripPrice(trip.getId(), order.getQuantity());

        //CREATE ORDER DETAIL
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetailList().stream().map(orderDetail -> {
            orderDetail.setOrder(order);
            return OrderDetailResponse.builder()
                    .Id(orderDetail.getId())
                    .participantName(orderDetail.getParticipantName())
                    .contact(orderDetail.getContact())
                    .build();
        }).toList();

        //CREATE ORDER STATUS
        statusService.createStatus(Status.builder()
                .status(EStatus.valueOf("WAITING"))
                .notes("NOTHING")
                        .order(order)
                .build());

        //CREATE PAYMENT
        return OrderResponse.builder()
                .id(order.getId())
                .user(order.getUser())
                .quantity(order.getQuantity())
                .orderDetailResponses(orderDetailResponses)
                .build();
    }
}
