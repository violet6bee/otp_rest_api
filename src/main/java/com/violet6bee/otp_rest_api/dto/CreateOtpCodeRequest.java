package com.violet6bee.otp_rest_api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOtpCodeRequest {
    private UUID operationUUID;
}
