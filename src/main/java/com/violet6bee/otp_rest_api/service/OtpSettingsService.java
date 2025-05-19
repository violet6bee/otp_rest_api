package com.violet6bee.otp_rest_api.service;

import com.violet6bee.otp_rest_api.entity.OtpSettingsEntity;
import com.violet6bee.otp_rest_api.repository.OtpSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpSettingsService {
    private final OtpSettingsRepository repository;


    /**
     * Создание или обновление настроек otp кодов.
     * Не дает создать больше одних настроек в БД.
     *
     * @param settings
     * @return настройки otp кодов.
     */
    public OtpSettingsEntity create(OtpSettingsEntity settings){
        Optional<OtpSettingsEntity> existingSettings = repository.findFirstByOrderByIdAsc();
        if (existingSettings.isPresent()) {
            settings.setId(existingSettings.get().getId());
        }
        return repository.save(settings);
    }

    public Optional<OtpSettingsEntity> getSettings () {
        return repository.findFirstByOrderByIdAsc();
    }
}
