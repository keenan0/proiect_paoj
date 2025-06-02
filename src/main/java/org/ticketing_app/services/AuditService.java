package org.ticketing_app.services;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuditService {

    private static final String FILE_NAME = "audit.csv";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logAction(String actionName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String line = actionName + "," + timestamp + "\n";

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
