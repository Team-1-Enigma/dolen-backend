package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class UploadImageController {

    private final UploadImageService uploadImageService;

    @PostMapping
    public ResponseEntity<CommonResponse<?>> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = uploadImageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<String>builder()
                .message("Image uploaded")
                .statusCode(HttpStatus.OK.value())
                .data(imageUrl)
                .build()

        );
    }
}
