package org.ticketing_app.access_code;

import org.springframework.stereotype.Component;

@Component
public class AccessCodeFactory {

    public AccessCode createAccessCode(String type) {
        return switch (type.toLowerCase()) {
            case "qr" -> new QrAccessCode();
            case "custom" -> new CustomAccessCode();
            default -> throw new IllegalArgumentException("Invalid access code type: " + type);
        };
    }
}