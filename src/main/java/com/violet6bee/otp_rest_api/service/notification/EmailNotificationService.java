package com.violet6bee.otp_rest_api.service.notification;

import com.violet6bee.otp_rest_api.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class EmailNotificationService implements NotificationService {

    private static final String SUBJECT = "OTP код";

    private final JavaMailSender emailSender;

    /**
     * {@inheritDoc}
     */
    public boolean sendOtpCode(final UserEntity user, final String otpCode) {
        try {
            final var simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(user.getEmail());
            simpleMailMessage.setSubject(SUBJECT);
            simpleMailMessage.setText(String.format("OTP: %s", otpCode));
            emailSender.send(simpleMailMessage);

            log.info("Сообщение Email отправлено успешно");

            return true;
        } catch (Exception e) {
            log.error("Ошибка отправки Email: {}", e.getMessage());
            return false;
        }
    }
}
