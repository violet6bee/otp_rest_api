package com.violet6bee.otp_rest_api.controller;

import com.violet6bee.otp_rest_api.dto.OtpSettingsRequest;
import com.violet6bee.otp_rest_api.entity.OtpSettingsEntity;
import com.violet6bee.otp_rest_api.service.OtpSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpSettingsController {
    private final OtpSettingsService service;

    @PostMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public OtpSettingsEntity setOtpSettings(@RequestBody @Valid OtpSettingsRequest settings) {
        return service.create(
                OtpSettingsEntity.builder()
                        .liveTime(settings.getLiveTime())
                        .symbolsNum(settings.getSymbolsNum())
                        .build()
        );
    }

}
