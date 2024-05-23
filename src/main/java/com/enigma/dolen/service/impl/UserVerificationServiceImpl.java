package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.model.entity.UserVerification;
import com.enigma.dolen.repository.UserVerificationRepository;
import com.enigma.dolen.service.UserVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class UserVerificationServiceImpl implements UserVerificationService {

    private final UserVerificationRepository userVerificationRepository;
    private final EmailService emailService;

    @Override
    public UserVerification createVerification(UserCredential userCredential, String url) {
        int randomCode = RandomGenerator.getDefault().nextInt(100000, 999999);
        UserVerification userVerification = userVerificationRepository.save(UserVerification.builder()
                .userCredential(userCredential)
                .verificationCode(randomCode)
                .isVerified(false)
                .build());
//        try {
//            sendVerificationEmail(userCredential, userVerification, url);
//        } catch (UnsupportedEncodingException e) {
//            throw new ApplicationException("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        return userVerification;
    }

    public void sendVerificationEmail(UserCredential userCredential, UserVerification userVerification, String url)
            throws UnsupportedEncodingException {
        String toAddress = userCredential.getEmail();
        String fromAddress = "qaultsabitm@gmail.com";
        String senderName = "Dolen";
        String subject = "Please verify your email address";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your email address:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Dolen.";

        content = content.replace("[[name]]", userCredential.getUser().getFullName());
        String verifyURL = url + "/api/verify?code=" + userVerification.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        emailService.sendEmail(toAddress, fromAddress, senderName, subject, content);
    }

    @Override
    public boolean verify(int verificationCode) {
        UserVerification userVerification = userVerificationRepository.findByVerificationCode(verificationCode);

        if (userVerification == null || userVerification.getIsVerified()) {
            return false;
        } else {
            userVerification.setIsVerified(true);
            userVerificationRepository.save(userVerification);
            return true;
        }
    }
}
