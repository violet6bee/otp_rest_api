package com.violet6bee.otp_rest_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpSettingsRequest {
    @NotNull(message = "Необходимо указать время жизни otp кода в секундах.")
    private Integer liveTime;

    @NotNull(message = "Необходимо указать длину otp кода.")
    private Integer symbolsNum;
}
