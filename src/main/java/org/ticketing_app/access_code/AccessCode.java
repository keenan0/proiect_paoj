package org.ticketing_app.access_code;

import java.time.LocalDateTime;

public abstract class AccessCode {
    protected String code;
    protected LocalDateTime generatedAt;

    public AccessCode() {
        this.generatedAt = LocalDateTime.now();
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public abstract void generate();

    @Override
    public String toString() {
        return "AccessCode{" +
                "code='" + code + '\'' +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
