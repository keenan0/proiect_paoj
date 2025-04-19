package org.ticketing_app.contoller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ticketing_app.model.TicketingEvent;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @PostMapping
    public ResponseEntity<String> addEvent(@RequestBody TicketingEvent event) {
        System.out.println(event);

        return ResponseEntity.status(HttpStatus.CREATED).body("Event created");
    }
}
