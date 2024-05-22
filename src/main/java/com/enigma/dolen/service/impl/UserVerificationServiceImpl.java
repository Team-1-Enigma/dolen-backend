package com.enigma.dolen.service.impl;

import com.enigma.dolen.config.MailConfig;
import com.enigma.dolen.model.entity.UserCredential;
import com.enigma.dolen.model.entity.UserVerification;
import com.enigma.dolen.model.exception.ApplicationException;
import com.enigma.dolen.repository.UserVerificationRepository;
import com.enigma.dolen.service.EmailService;
import com.enigma.dolen.service.UserVerificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class UserVerificationServiceImpl implements UserVerificationService {

    private final UserVerificationRepository userVerificationRepository;
    private final EmailService emailService;
    private final MailConfig mailConfig;

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
//        } catch (UnsupportedEncodingException | MessagingException e) {
//            throw new ApplicationException("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        return userVerification;
    }

    public void sendVerificationEmail(UserCredential userCredential, UserVerification userVerification, String url)
            throws UnsupportedEncodingException, MessagingException {
        String toAddress = userCredential.getEmail();
        String fromAddress = "wanderermateid@gmail.com";
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

        MimeMessage message = mailConfig.javaMailSender().createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom(fromAddress, senderName);
        messageHelper.setTo(toAddress);
        messageHelper.setSubject(subject);

        messageHelper.setText(content, true);
//        emailService.sendEmail(toAddress, fromAddress, senderName, subject, content);
        mailConfig.javaMailSender().send(message);
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
