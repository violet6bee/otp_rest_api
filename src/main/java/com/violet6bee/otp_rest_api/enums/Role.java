package com.violet6bee.otp_rest_api.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ROLE_USER ("User"),
    ROLE_ADMIN ("Admin");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
