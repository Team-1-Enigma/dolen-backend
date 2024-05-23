package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.OrderDetailRequest;
import com.enigma.dolen.model.dto.OrderDetailResponse;
import com.enigma.dolen.model.entity.OrderDetail;
import com.enigma.dolen.repository.OrderDetailRepository;
import com.enigma.dolen.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailRequest orderDetailRequest) {
        OrderDetail orderDetail = orderDetailRepository.save(
                OrderDetail.builder()
                        .participantName(orderDetailRequest.getParticipantName())
                        .contact(orderDetailRequest.getContact())
                        .order(orderDetailRequest.getOrder())
                        .build()
        );
        return OrderDetailResponse.builder()
                .Id(orderDetail.getId())
                .participantName(orderDetail.getParticipantName())
                .contact(orderDetail.getContact())
                .order(orderDetail.getOrder())
                .build();
    }
}
