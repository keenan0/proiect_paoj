package org.ticketing_app.access_code;

public class NullAccessCode extends AccessCode {
    @Override
    public void generate() {
        this.code = "NONE";
    }

    private String generateCode() {
        return "NULL";
    }
}