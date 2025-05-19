package com.violet6bee.otp_rest_api.dto;

import com.violet6bee.otp_rest_api.enums.Status;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CheckOtpCodeResponse {
    public Status status;
}
