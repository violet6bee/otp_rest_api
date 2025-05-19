package com.violet6bee.otp_rest_api.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    ACTIVE ("Active"),
    EXPIRED ("Expired"),
    USED ("Used");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
