package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.EPaymentStatus;
import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.dto.MidtransStatusResponse;
import com.enigma.dolen.model.dto.ParticipantDTO;
import com.enigma.dolen.model.dto.PaymentRequest;
import com.enigma.dolen.model.dto.PaymentResponse;
import com.enigma.dolen.model.entity.Order;
import com.enigma.dolen.model.entity.OrderDetail;
import com.enigma.dolen.model.entity.Payment;
import com.enigma.dolen.repository.OrderRepository;
import com.enigma.dolen.repository.PaymentRepository;
import com.enigma.dolen.service.MidtransService;
import com.enigma.dolen.service.ParticipantService;
import com.enigma.dolen.service.StatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import com.enigma.dolen.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final MidtransService midtransService;
    private final OrderRepository orderRepository;
    private final ParticipantService participantService;
    private final StatusService statusService;
    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        PaymentResponse midtransCreatePaymentLink = midtransService.createTransaction(paymentRequest);

        Payment payment =
                Payment.builder()
                        .Id(midtransCreatePaymentLink.getId())
                        .paymentLink(midtransCreatePaymentLink.getPaymentLink())
                        .paymentStatus(midtransCreatePaymentLink.getPaymentStatus())
                        .total(midtransCreatePaymentLink.getTotal())
                        .order(paymentRequest.getOrder())
                        .build();
        paymentRepository.saveAndFlush(payment);

        return PaymentResponse.builder()
                .Id(payment.getId())
                .paymentLink(payment.getPaymentLink())
                .paymentStatus(payment.getPaymentStatus())
                .total(payment.getTotal())
                .order(payment.getOrder())
                .build();
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.getPaymentByOrder_Id(orderId).orElseThrow(()-> new EntityNotFoundException("No Payment Found"));

        return PaymentResponse.builder()
                .Id(payment.getId())
                .paymentLink(payment.getPaymentLink())
                .paymentStatus(payment.getPaymentStatus())
                .total(payment.getTotal())
                .build();
    }

    @Override
    public MidtransStatusResponse checkIsPaymentSuccess(String orderId) {
        Payment payment = paymentRepository.getPaymentByOrder_Id(orderId).orElseThrow(()-> new EntityNotFoundException("No Payment Found"));



        MidtransStatusResponse midtransStatusResponse = midtransService.getPaymentStatus(payment.getId());


        Order order = payment.getOrder();
        List<OrderDetail> orderDetail = order.getOrderDetailList();

        if(Objects.equals(midtransStatusResponse.getTransaction_status(), "settlement")){
            payment.setPaymentStatus(EPaymentStatus.SETTLEMENT);
            statusService.changeStatus(EStatus.ACTIVE, orderId);
            paymentRepository.saveAndFlush(payment);

            List<ParticipantDTO> participantDTOList = orderDetail.stream().map(orderDetail1 -> {
                        ParticipantDTO participantDTO = ParticipantDTO.builder()
                                .participantName(orderDetail1.getParticipantName())
                                .contact(orderDetail1.getContact())
                                .tripId(order.getTrip().getId()).build();

                        participantService.createParticipant(participantDTO);

                        return participantDTO;
                    })
                    .toList();
        }

        return midtransStatusResponse;
    }

}
