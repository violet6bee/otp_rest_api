package com.violet6bee.otp_rest_api.service;

import com.violet6bee.otp_rest_api.dto.CheckOtpCodeResponse;
import com.violet6bee.otp_rest_api.entity.OtpSettingsEntity;
import com.violet6bee.otp_rest_api.entity.OtpTableEntity;
import com.violet6bee.otp_rest_api.entity.UserEntity;
import com.violet6bee.otp_rest_api.enums.Status;
import com.violet6bee.otp_rest_api.repository.OtpRepository;
import com.violet6bee.otp_rest_api.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final List<NotificationService> notificationServices;
    private final OtpSettingsService otpSettingsService;
    @Value("${otp-settings:default-live-time}")
    private String otpCodeLiveTimeDefault;

    public void createOtpCode(UUID operationUUID, UserEntity user){
        Random random = new Random();
        StringBuilder code = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            int digit = random.nextInt(10);
            code.append(digit);
        }

        LocalDateTime createDateTime = LocalDateTime.now();

        var otpEntity = OtpTableEntity.builder()
                .user(user)
                .operationUuid(operationUUID)
                .code(code.toString())
                .status(Status.ACTIVE)
                .createDateTime(createDateTime)
                .build();

        otpRepository.save(otpEntity);
        for(var service : notificationServices) {
            service.sendOtpCode(user, code.toString());
        }
    }

    private boolean checkOtpCodeValid(OtpTableEntity otpCodeEntity) {
        Optional<OtpSettingsEntity> otpSettings = otpSettingsService.getSettings();
        Integer otpCodeLiveTime;
        if (otpSettings.isPresent()){
            otpCodeLiveTime = otpSettings.get().getLiveTime();
        } else {
            otpCodeLiveTime = Integer.parseInt(otpCodeLiveTimeDefault);
        }
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(otpCodeEntity.getCreateDateTime(), now);
        if (duration.getSeconds() < otpCodeLiveTime) {
            return true;
        }
        return false;
    }

    public CheckOtpCodeResponse checkOtpCode(String code, UUID operationUUID) {
        OtpTableEntity otpCode = new OtpTableEntity();
        otpCode.setCode(code);
        otpCode.setOperationUuid(operationUUID);
        Example<OtpTableEntity> example = Example.of(otpCode);
        Optional<OtpTableEntity> result = otpRepository.findOne(example);
        if (result.isPresent()) {
            otpCode = result.get();
            Status status = otpCode.getStatus();
            if (!checkOtpCodeValid(otpCode)) {
                otpCode.setStatus(Status.EXPIRED);
            } else {
                otpCode.setStatus(Status.USED);
            }
            otpRepository.save(otpCode);
            return CheckOtpCodeResponse.builder().status(status).build();
        }
        throw new RuntimeException("Код для данной операции не найден.");
    }

    public InputStreamResource getUserCodes (UserEntity user) {
        OtpTableEntity otpCodeEntity = new OtpTableEntity();
        otpCodeEntity.setUser(user);
        Example<OtpTableEntity> example = Example.of(otpCodeEntity);
        List<OtpTableEntity> otpCodes = otpRepository.findAll(example);
        StringBuilder sb = new StringBuilder();
        sb.append("ID, Operation UUID, Code, Status, Create Date\n");

        for (OtpTableEntity otpCode : otpCodes) {
            sb.append(otpCode.getId()).append(", ")
                    .append(otpCode.getOperationUuid()).append(", ")
                    .append(otpCode.getCode()).append(", ")
                    .append(otpCode.getStatus()).append(", ")
                    .append(otpCode.getCreateDateTime()).append("\n");
        }

        byte[] bytes = sb.toString().getBytes();

        return new InputStreamResource(new ByteArrayInputStream(bytes));
    }

}
