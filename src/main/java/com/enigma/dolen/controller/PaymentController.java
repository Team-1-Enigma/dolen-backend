package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Payment;
import com.enigma.dolen.repository.PaymentRepository;
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
    private final PaymentService paymentService;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable String id){
        PaymentResponse paymentResponse = paymentService.getPaymentByOrderId(id);

        CommonResponse<?> response = CommonResponse.<PaymentResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payment Found")
                .data(paymentResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<?> checkPaymentStatus(@PathVariable String id){
        MidtransStatusResponse paymentResponse = paymentService.checkIsPaymentSuccess(id);

        CommonResponse<?> response = CommonResponse.<MidtransStatusResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payment Found")
                .data(paymentResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/beneficiaries")
    public ResponseEntity<?> createBeneficiaries(@RequestBody BeneficiariesRequest beneficiariesRequest){
        BeneficiariesResponse beneficiaries = midtransService.createBeneficiaries(beneficiariesRequest);

        CommonResponse<?> response = CommonResponse.<BeneficiariesResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payment Found")
                .data(beneficiaries)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/payout")
    public ResponseEntity<?> createPayout(@RequestBody PayoutRequest payoutRequest){
        PayoutResponse payout = midtransService.createPayout(payoutRequest);

        CommonResponse<?> response = CommonResponse.<PayoutResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Payment Found")
                .data(payout)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
