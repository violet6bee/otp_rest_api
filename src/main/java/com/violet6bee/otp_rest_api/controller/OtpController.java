package com.violet6bee.otp_rest_api.controller;

import com.violet6bee.otp_rest_api.dto.CheckOtpCodeRequest;
import com.violet6bee.otp_rest_api.dto.CheckOtpCodeResponse;
import com.violet6bee.otp_rest_api.dto.CreateOtpCodeRequest;
import com.violet6bee.otp_rest_api.entity.UserEntity;
import com.violet6bee.otp_rest_api.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService service;

    @PostMapping("/create")
    public ResponseEntity<String> createOtp(@RequestBody @Valid CreateOtpCodeRequest request, @AuthenticationPrincipal UserEntity currentUser) {
        service.createOtpCode(request.getOperationUUID(), currentUser);
        return ResponseEntity.ok("OTP-код создан.");
    }

    @PostMapping("/check")
    public CheckOtpCodeResponse checkOtpCodeStatus(@RequestBody @Valid CheckOtpCodeRequest request) {
        CheckOtpCodeResponse response = service.checkOtpCode(request.getCode(), request.getOperationUUID());
        return response;
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadOtpCodes(@AuthenticationPrincipal UserEntity currentUser) throws IOException {
        InputStreamResource resource = service.getUserCodes(currentUser);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=otp_codes.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

}
