package com.enigma.dolen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {

    @Autowired
    private SesClient sesClient;

    public void sendEmail(String toAddress, String fromAddress, String senderName, String subject, String content) {
        Destination destination = Destination.builder()
                .toAddresses(toAddress)
                .build();

        Content subjectContent = Content.builder()
                .data(subject)
                .build();

        Content bodyContent = Content.builder()
                .data(content)
                .build();

        Body body = Body.builder()
                .html(bodyContent)
                .build();

        Message message = Message.builder()
                .subject(subjectContent)
                .body(body)
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(fromAddress)
                .build();

        sesClient.sendEmail(request);
    }
}
