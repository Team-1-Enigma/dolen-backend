package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.service.UserVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verify")
public class VerificationController {

    private final UserVerificationService userVerificationService;

    @GetMapping
    public ResponseEntity<CommonResponse<?>> verifyUser(@RequestParam int code) {
        boolean isVerified = userVerificationService.verify(code);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.<Boolean>builder()
                .message("User verified")
                .statusCode(HttpStatus.OK.value())
                .data(isVerified)
                .build()
        );
    }
}
