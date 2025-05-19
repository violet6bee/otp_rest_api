package com.violet6bee.otp_rest_api.service.notification;

import com.violet6bee.otp_rest_api.entity.UserEntity;

public interface
NotificationService {
    boolean sendOtpCode(UserEntity user, String otpCode);
}
