package com.violet6bee.otp_rest_api.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CheckOtpCodeRequest {
    public String code;
    public UUID operationUUID;
}