package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Order;

public interface MidtransService {
    PaymentResponse createTransaction(PaymentRequest paymentRequest);
    MidtransStatusResponse getPaymentStatus(String paymentId);

    BeneficiariesResponse createBeneficiaries(BeneficiariesRequest beneficiariesRequest);

    PayoutResponse createPayout (PayoutRequest payoutRequest);

    BeneficiariesResponse approvePayput(PayoutApproveRequest payoutApproveRequest);

}
