package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.EGender;
import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.dto.CreateOrderRequest;
import com.enigma.dolen.model.dto.OrderDetailResponse;
import com.enigma.dolen.model.dto.OrderResponse;
import com.enigma.dolen.model.dto.UserDTO;
import com.enigma.dolen.model.entity.Order;
import com.enigma.dolen.model.entity.OrderDetail;
import com.enigma.dolen.model.entity.Status;
import com.enigma.dolen.model.entity.User;
import com.enigma.dolen.repository.OrderRepository;
import com.enigma.dolen.service.OrderService;
import com.enigma.dolen.service.StatusService;
import com.enigma.dolen.service.UserService;
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
    @Override
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

        //CREATE ORDER
        Order order = orderRepository.saveAndFlush(Order.builder()
                        .orderDetailList(orderDetailList)
                        .user(user)
                        .quantity(orderRequest.getQuantity())
                .build()
        );

        //TODO:REDUCE OPEN TRIP AVAILABLE QUOTA

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
