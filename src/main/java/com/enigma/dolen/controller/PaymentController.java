package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.PaymentRequest;
import com.enigma.dolen.model.dto.PaymentResponse;
import com.enigma.dolen.service.MidtransService;
import com.enigma.dolen.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final MidtransService midtransService;
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest){
        PaymentResponse paymentResponse = midtransService.createTransaction(paymentRequest);

        CommonResponse<?> response = CommonResponse.<PaymentResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Created")
                .data(paymentResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
