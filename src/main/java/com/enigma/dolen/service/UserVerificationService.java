package com.enigma.dolen.service;

import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.model.entity.UserVerification;

public interface UserVerificationService {

    UserVerification createVerification(UserCredential userCredential, String url);

    boolean verify(int verificationCode);
}
