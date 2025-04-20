package org.ticketing_app.access_code;

public class CustomAccessCode extends AccessCode {
    @Override
    public void generate() {
        this.code = generateCode();
    }

    private String generateCode() {
        return "temp";
    }
}
