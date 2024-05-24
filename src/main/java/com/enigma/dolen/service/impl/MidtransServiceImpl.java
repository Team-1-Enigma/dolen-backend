package com.enigma.dolen.service.impl;

import com.enigma.dolen.config.RestTemplateConfiguration;
import com.enigma.dolen.constant.EPaymentStatus;
import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Order;
import com.enigma.dolen.model.entity.OrderDetail;
import com.enigma.dolen.model.entity.TransactionDetails;
import com.enigma.dolen.service.MidtransService;
import com.enigma.dolen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MidtransServiceImpl implements MidtransService {

    private final RestTemplate restTemplateConfiguration;

    @Override
    public PaymentResponse createTransaction(PaymentRequest paymentRequest) {


        String url = "https://app.sandbox.midtrans.com/snap/v1/transactions";

        paymentRequest.setPayment_id(String.valueOf(System.currentTimeMillis()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("SB-Mid-server-H0f7IN4TeinYP1I6qvFwMuNh:".getBytes()));

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .order_id(paymentRequest.getPayment_id())
                .gross_amount(paymentRequest.getTotal())
                .build();

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setTransaction_details(transactionDetails);

        HttpEntity<TransactionRequest> entity = new HttpEntity<>(transactionRequest, headers);

        ResponseEntity<MidtransResponse> response = restTemplateConfiguration.exchange(
                url,
                HttpMethod.POST,
                entity,
                MidtransResponse.class
        );

        MidtransResponse midtransResponse = response.getBody();

        assert midtransResponse != null;
        return PaymentResponse.builder()
                .Id(paymentRequest.getPayment_id())
                .paymentLink(midtransResponse.getRedirect_url())
                .total(paymentRequest.getTotal())
                .order(paymentRequest.getOrder())
                .paymentStatus(EPaymentStatus.WAITING)
                .build();
    }
}
