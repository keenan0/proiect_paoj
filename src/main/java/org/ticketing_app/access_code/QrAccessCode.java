package org.ticketing_app.access_code;

import java.util.UUID;

public class QrAccessCode extends AccessCode {
    @Override
    public void generate() {
        this.code = UUID.randomUUID().toString();
    }
}
