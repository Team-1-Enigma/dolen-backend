package com.enigma.dolen.controller;

import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest orderRequest){
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        CommonResponse<?> commonResponse = CommonResponse.<OrderResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Status Created")
                .data(orderResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId){
        OrderResponse orderResponse = orderService.getOrderById(orderId);

        CommonResponse<?> commonResponse = CommonResponse.<OrderResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("ORDER FOUND")
                .data(orderResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/{userId}/{status}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable String userId, @PathVariable EStatus status){
        List<OrderResponse> orderResponseList = orderService.getOrderByUserId(userId, status);

        CommonResponse<?> commonResponse = CommonResponse.<List<OrderResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("ORDER FOUND")
                .data(orderResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/{tripId}/{status}/trip")
    public ResponseEntity<?> getOrderByTripId(@PathVariable String tripId, @PathVariable EStatus status){
        List<OrderResponse> orderResponseList = orderService.getOrderByTripId(tripId, status);

        CommonResponse<?> commonResponse = CommonResponse.<List<OrderResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("ORDER FOUND")
                .data(orderResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PostMapping("/payout/{orderId}")
    public ResponseEntity<?> payout(@PathVariable String orderId){
        BeneficiariesResponse beneficiaries = orderService.payout(orderId);

        CommonResponse<?> response = CommonResponse.<BeneficiariesResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payout Success")
                .data(beneficiaries)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/participant/{tripId}")
    public ResponseEntity<?> getAllParticipant(@PathVariable String tripId){
        List<ParticipantDTO> participantDTO = orderService.getParticipant(tripId);
        CommonResponse<?> response = CommonResponse.<List<ParticipantDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get All Participant success")
                .data(participantDTO)
                .build();


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
