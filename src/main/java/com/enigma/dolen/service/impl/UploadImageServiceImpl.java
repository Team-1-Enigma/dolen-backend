package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.exception.ApplicationException;
import com.enigma.dolen.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UploadImageServiceImpl implements UploadImageService {

    private final S3Client s3Client;
    @Value("${aws.bucket.name}")
    private String bucketName;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            String fileName = "user-images/" + System.currentTimeMillis();
            s3Client.putObject(builder -> builder.bucket(bucketName).key(fileName).build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
        } catch (IOException e) {
            throw new ApplicationException("Failed to upload photo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
