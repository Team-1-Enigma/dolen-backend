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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
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

    @Override
    public MidtransStatusResponse getPaymentStatus(String paymentId) {
        String url = "https://api.sandbox.midtrans.com/v2/" + paymentId + "/status";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString("SB-Mid-server-H0f7IN4TeinYP1I6qvFwMuNh:".getBytes()));
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<MidtransStatusResponse> response = restTemplateConfiguration.exchange(
                url,
                HttpMethod.GET,
                entity,
                MidtransStatusResponse.class
        );

        return response.getBody();
    }


    @Override
    public BeneficiariesResponse createBeneficiaries(BeneficiariesRequest beneficiariesRequest) {

        String url  ="https://app.midtrans.com/iris/api/v1/beneficiaries";


        String auth = "IRIS-4d92162d-ab08-4d64-964e-30b99c56754d" + ":";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("Content-Type", "application/json");

        HttpEntity<BeneficiariesRequest> entity = new HttpEntity<>(beneficiariesRequest, headers);

        try{
            ResponseEntity<BeneficiariesResponse> response = restTemplateConfiguration.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    BeneficiariesResponse.class
            );

            BeneficiariesResponse beneficiariesResponse = response.getBody();

            assert beneficiariesResponse != null;

            return BeneficiariesResponse.builder()
                    .status(beneficiariesResponse.getStatus())
                    .build();
        }catch (HttpClientErrorException e){
            return null;
        }


    }

    @Override
    public PayoutResponse createPayout(PayoutRequest payoutRequest) {

        final String auth = "IRIS-4d92162d-ab08-4d64-964e-30b99c56754d" + ":";
        final String baseUrl = "https://app.midtrans.com/iris/api/v1/payouts";

        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.set("Content-Type", "application/json");

        HttpEntity<PayoutRequest> entity = new HttpEntity<>(payoutRequest, headers);

        try {
            ResponseEntity<PayoutResponse> response = restTemplateConfiguration.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    PayoutResponse.class
            );


            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("Error occurred while creating payout: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public BeneficiariesResponse approvePayput(PayoutApproveRequest payoutApproveRequest) {
        final String auth = "IRIS-c3bb498c-083a-4610-a80d-ab6ea07b4450" + ":";
        final String baseUrl = "https://app.midtrans.com/iris/api/v1/payouts/approve";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        byte[] authBytes = auth.getBytes();
        headers.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(authBytes));

        HttpEntity<PayoutApproveRequest> entity = new HttpEntity<>(payoutApproveRequest, headers);

        try {
            ResponseEntity<BeneficiariesResponse> response = restTemplateConfiguration.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    BeneficiariesResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error occurred while approving payouts: " + e.getMessage());
            return null;
        }
    }
}
