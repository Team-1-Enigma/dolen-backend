package com.enigma.dolen.service.impl;

import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.*;
import com.enigma.dolen.repository.OrderRepository;
import com.enigma.dolen.repository.UserCredentialRepository;
import com.enigma.dolen.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final StatusService statusService;
    private final TripService tripService;
    private final TripPriceService tripPriceService;
    private final PaymentService paymentService;
    private final MidtransService midtransService;
    private final TravelService travelService;
    private final BankAccountService bankAccountService;
    private final UserCredentialRepository userCredentialRepository;
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

        //TODO CREATE PAYMENT
        TripPriceResponse tripPrice = tripPriceService.getTripPriceByTripId(trip.getId()).get(0);
        Long total = tripPrice.getPrice() * orderRequest.getQuantity();

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .order(order)
                .total(total)
                .build();
        paymentService.createPayment(paymentRequest);

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

    @Override
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->  new EntityNotFoundException("Order Not Found"));

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetailList().stream().map(orderDetail -> {
            return OrderDetailResponse.builder()
                    .Id(orderDetail.getId())
                    .participantName(orderDetail.getParticipantName())
                    .contact(orderDetail.getContact())
                    .build();
        }).toList();
        Trip trip = order.getTrip();
        TripResponse tripResponse = TripResponse.builder()
                .id(trip.getId())
                .travelId(trip.getId())
                .destination(trip.getDestination())
                .departureDate(String.valueOf(trip.getDepartureDate()))
                .returnDate(String.valueOf(trip.getReturnDate()))
                .build();
        return OrderResponse.builder()
                .id(order.getId())
                .user(order.getUser())
                .trip(tripResponse)
                .quantity(order.getQuantity())
                .order_date(LocalTime.from(order.getOrder_date()))
                .orderDetailResponses(orderDetailResponses)
                .build();
    }

    @Override
    public List<OrderResponse> getOrderByUserId(String userId, EStatus eStatus) {

        List<Order> orderList = orderRepository.findOrdersByUserId(userId).orElseThrow(()->new EntityNotFoundException("Order Response not found"));

        List<OrderResponse> orderResponseList = orderList.stream().map(order -> {
            Trip trip = order.getTrip();
            TripResponse tripResponse = TripResponse.builder()
                    .id(trip.getId())
                    .travelId(trip.getId())
                    .destination(trip.getDestination())
                    .departureDate(String.valueOf(trip.getDepartureDate()))
                    .returnDate(String.valueOf(trip.getReturnDate()))
                    .build();

            List<OrderDetailResponse> orderDetailResponses = order.getOrderDetailList().stream().map(orderDetail -> {
                return OrderDetailResponse.builder()
                        .Id(orderDetail.getId())
                        .participantName(orderDetail.getParticipantName())
                        .contact(orderDetail.getContact())
                        .build();
            }).toList();

            Status status = statusService.getStatus(order.getId());
            if(status.getStatus() == eStatus){
                return OrderResponse.builder()
                        .id(order.getId())
                        .user(order.getUser())
                        .trip(tripResponse)
                        .quantity(order.getQuantity())
                        .order_date(LocalTime.from(order.getOrder_date()))
                        .orderDetailResponses(orderDetailResponses)
                        .build();
            } else {
                return null;
            }

        }).toList();

        return orderResponseList;
    }

    @Override
    public List<OrderResponse> getOrderByTripId(String tripId, EStatus eStatus) {
        Trip trip = tripService.getTripByIdForOther(tripId);

        List<Order> orderList = orderRepository.findOrderByTripId(tripId).orElseThrow(()->new EntityNotFoundException("Order Response not found"));

        List<OrderResponse> orderResponseList = orderList.stream().map(order -> {
            List<OrderDetailResponse> orderDetailResponses = order.getOrderDetailList().stream().map(orderDetail -> {
                return OrderDetailResponse.builder()
                        .Id(orderDetail.getId())
                        .participantName(orderDetail.getParticipantName())
                        .contact(orderDetail.getContact())
                        .build();
            }).toList();

            Status status = statusService.getStatus(order.getId());
            if(status.getStatus() == eStatus){
                return OrderResponse.builder()
                        .id(order.getId())
                        .user(order.getUser())
                        .trip(TripResponse.builder()
                                        .id(trip.getId())
                                        .destination(trip.getDestination())
                                        .departureDate(String.valueOf(trip.getDepartureDate()))
                                        .returnDate(String.valueOf(trip.getReturnDate()))
                                .build())
                        .quantity(order.getQuantity())
                        .order_date(LocalTime.from(order.getOrder_date()))
                        .orderDetailResponses(orderDetailResponses)
                        .build();
            } else {
                return null;
            }

        }).toList();
        return orderResponseList;
    }

    @Override
    public BeneficiariesResponse payout(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        TravelCreateResponse travel = travelService.getTravelById(order.getTrip().getTravel().getId());
        BankAccountResponse bankAccount = bankAccountService.getAllBankAccountByTravelId(travel.getId()).get(0);
        UserCredential userCredential = userCredentialRepository.findByUserId(travel.getUserId()).orElseThrow(()-> new EntityNotFoundException("NOt FOund"));
        UserDTO user = userService.getUserById(userCredential.getId());
        PaymentResponse payment = paymentService.getPaymentByOrderId(orderId);

        BeneficiariesResponse beneficiariesResponse =  midtransService.createBeneficiaries(
                BeneficiariesRequest.builder()
                        .name(bankAccount.getName())
                        .bank(bankAccount.getBankName())
                        .email(user.getEmail())
                        .alias_name(bankAccount.getAliasName())
                          .build()
        );
        PayoutRequest.Payout payout = PayoutRequest.Payout.builder()
                .beneficiaryName(bankAccount.getName())
                .beneficiaryAccount(bankAccount.getAccountNumber())
                .beneficiaryEmail(user.getEmail())
                .beneficiaryBank(bankAccount.getBankName())
                .amount(String.valueOf(payment.getTotal()))
                .notes("Payout dari Dolen")
                .build();

        PayoutRequest request = PayoutRequest.builder()
                .payouts(Collections.singletonList(payout))
                .build();

        PayoutResponse payoutResponse = midtransService.createPayout(request);
        midtransService.approvePayput(PayoutApproveRequest.builder()
                        .otp("335163")
                        .referenceNos(Collections.singletonList(payoutResponse.getPayouts().get(0).getReferenceNo()))
                .build());

        statusService.changeStatus(EStatus.ACCEPTED, orderId);

        return beneficiariesResponse;
    }

    @Override
    public List<ParticipantDTO> getParticipant(String tripId) {
        List<Order> orderList = orderRepository.findOrderByTripId(tripId).orElseThrow(()->new RuntimeException("No data"));

        orderList = orderList.stream()
                .filter(order -> {
                    Status status = statusService.getStatus(order.getId());

                    return status.getStatus() == EStatus.ACTIVE;
                }) // Ganti dengan kondisi yang sesuai dengan struktur status Anda
                .toList();


        List<OrderDetail> orderDetailList = orderList.stream()
                .flatMap(order -> order.getOrderDetailList().stream()
                        .map(orderDetail -> OrderDetail.builder()
                                .participantName(orderDetail.getParticipantName())
                                .order(order)
                                .contact(orderDetail.getContact())
                                .build()
                        )
                )
                .toList();

        List<ParticipantDTO> participantDTOList = orderDetailList.stream().map(orderDetail -> {
            return ParticipantDTO.builder()
                    .participantName(orderDetail.getParticipantName())
                    .contact(orderDetail.getContact())
                    .build();
        }).toList();
        return participantDTOList;
    }


}
