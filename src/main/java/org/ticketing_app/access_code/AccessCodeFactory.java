package org.ticketing_app.access_code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ticketing_app.services.AuditService;

@Component
public class AccessCodeFactory {

    @Autowired
    private AuditService auditService;

    public AccessCode createAccessCode(String type) {
        auditService.logAction("AccessCodeFactory: Create access code with type: " + type);

        if (type == null) {
            return new NullAccessCode();
        }

        return switch (type.toLowerCase()) {
            case "qr" -> new QrAccessCode();
            case "custom" -> new CustomAccessCode();
            default -> throw new IllegalArgumentException("Invalid access code type: " + type);
        };
    }
}